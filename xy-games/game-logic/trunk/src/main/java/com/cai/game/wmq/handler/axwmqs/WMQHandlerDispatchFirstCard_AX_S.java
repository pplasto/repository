/**
 * 
 */
package com.cai.game.wmq.handler.axwmqs;

/**
 * @author xwy
 *
 */

import java.util.concurrent.TimeUnit;

import com.cai.common.constant.GameConstants;
import com.cai.common.domain.ChiHuRight;
import com.cai.common.domain.PlayerStatus;
import com.cai.future.GameSchedule;
import com.cai.future.runnable.GameFinishRunnable;
import com.cai.future.runnable.HuPaiRunnable;
import com.cai.future.runnable.OutCardRunnable;
import com.cai.game.wmq.WMQTable;
import com.cai.game.wmq.handler.WMQHandlerDispatchCard;

public class WMQHandlerDispatchFirstCard_AX_S extends WMQHandlerDispatchCard<WMQTable> {

	@Override
	public void exe(WMQTable table) {
		// 用户状态
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			table._playerStatus[i].clean_action();
			table._playerStatus[i].clean_status();
		}

		table._playerStatus[_seat_index].chi_hu_round_valid();// 可以胡了

		// 荒庄结束
		if (table.GRR._left_card_count == 0) {
			for (int i = 0; i < table.getTablePlayerNumber(); i++) {
				table.GRR._chi_hu_card[i][0] = GameConstants.INVALID_VALUE;
			}
			table._cur_banker = (table.GRR._banker_player + table.getTablePlayerNumber() + 1)
					% table.getTablePlayerNumber();
			table._shang_zhuang_player = GameConstants.INVALID_SEAT;
			// 流局
			table.operate_dou_liu_zi(-1,false ,0,false);
	
			GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), table._cur_banker, GameConstants.Game_End_DRAW),
					2, TimeUnit.SECONDS);

			return;
		}
		boolean is_hu = false;
		table._current_player = _seat_index;// 轮到操作的人是自己
		
		// 从牌堆拿出一张牌
		table._send_card_count++;
		_send_card_data = table._repertory_card[table._all_card_len - table.GRR._left_card_count];
		--table.GRR._left_card_count;
		table.operate_player_get_card(_seat_index, 1, new int[] { _send_card_data }, GameConstants.INVALID_SEAT,true);
		
//		_send_card_data = 0x18;
		table._send_card_data = _send_card_data;
		table._provide_player = _seat_index;
		int send_index  = table._logic.switch_to_card_index(_send_card_data);
		boolean is_fa_pai = false;
		int loop = 0;
		while(loop < table.getTablePlayerNumber()){
			int i = (table._current_player+loop) %table.getTablePlayerNumber() ; 
			loop ++;
			ChiHuRight chr = table.GRR._chi_hu_rights[i];
			PlayerStatus curPlayerStatus = table._playerStatus[i];
    		curPlayerStatus.reset();
    		int is_common[] = new int[1];
			if(table.is_tian_hu_s(i,table._current_player ,chr, table._send_card_data ,is_common) == true)
			{     		
				// 添加动作
        		if(i == table._cur_banker)
        			table.GRR._cards_index[table._cur_banker][table._logic.switch_to_card_index(table._send_card_data)]++;
				curPlayerStatus.add_action(GameConstants.WIK_ZI_MO);
				curPlayerStatus.add_zi_mo(_send_card_data, i);
				if(table.getRuleValue(GameConstants.GAME_RULE_XIAO_ZHUO_BAN) != 1&&is_common[0] == 1)
				{
					curPlayerStatus.add_action(GameConstants.WIK_ZHE_WMQ);
					curPlayerStatus.add_zhe(_send_card_data,GameConstants.WIK_ZHE_WMQ,i);
				}
				
				curPlayerStatus.add_action(GameConstants.WIK_NULL);
				curPlayerStatus.add_pass(_send_card_data, i);	
				if(table.has_rule(GameConstants.GAME_RULE_QIANG_HU_PAI))
				{
					GameSchedule.put(new HuPaiRunnable(table.getRoom_id(), i, GameConstants.WIK_ZI_MO,_send_card_data),
							600, TimeUnit.MILLISECONDS);
					return ;
				}
				
				 if (curPlayerStatus.has_action()) {
					  curPlayerStatus.set_status(GameConstants.Player_Status_OPR_CARD);//
					 //操作状态
					  table.operate_player_action(i, false);
					 }
				 is_hu = true;
        	}
        	else{
				chr.set_empty();
			}	        	
	        
		
			
		}
	
		if(is_hu == false)
		{
			// 发送数据
			// 只有自己才有数值
	
//			table.operate_player_get_card(_seat_index, 1, new int[] { _send_card_data }, GameConstants.INVALID_SEAT,true);
			
			// 设置变量
			table._provide_card = _send_card_data;// 提供的牌
	
			table.exe_chuli_first_card(_seat_index,GameConstants.WIK_NULL,1000);
			_send_card_data = 0;
		}
		return;
	}

	/***
	 * //用户出牌
	 */
	@Override
	public boolean handler_player_out_card(WMQTable table, int seat_index, int card) {
		// 错误断言
		card = table.get_real_card(card);

		if (table._logic.is_valid_card(card) == false) {
			table.log_error("出牌,牌型出错");
			return false;
		}

		// 效验参数
		if (seat_index != _seat_index) {
			table.log_error("出牌,没到出牌");
			return false;
		}

		// 删除扑克
		if (table._logic.remove_card_by_index(table.GRR._cards_index[_seat_index], card) == false) {
			table.log_error("出牌删除出错");
			return false;
		}

		// 出牌
		// if(_type == MJGameConstants.DispatchCard_Type_Gang){
		// table.exe_re_chong(_seat_index,card,_type);
		// }else {
		// table.exe_out_card(_seat_index,card,_type);
		// }
		table.exe_out_card(_seat_index, card, _type);

		return true;
	}

	/***
	 * //用户操作
	 * 
	 * @param seat_index
	 * @param operate_code
	 * @param operate_card
	 * @param luoCode
	 * @return
	 */
	@Override
	public boolean handler_operate_card(WMQTable table,int seat_index, int operate_code, int operate_card,int luoCode){
		PlayerStatus playerStatus = table._playerStatus[seat_index];
		
		
		// 效验操作
		if ((operate_code != GameConstants.WIK_NULL) && (playerStatus.has_action_by_code(operate_code) == false)) {
			table.log_error("DispatchCard 没有这个操作:" + operate_code);
			return false;
		}
		table.record_effect_action(seat_index,GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[]{operate_code}, 1);
		
		if(operate_code == GameConstants.WIK_ZHE_WMQ)
		{
			if(playerStatus.has_zi_mo())
			{
				table._zhe_zhe_count[seat_index]++;
			}
		}
	
		// 是否已经响应
		if (playerStatus.has_action() == false) {
			table.log_player_error(seat_index, "DispatchCard 出牌,玩家操作已失效");
			return true;
		}
		//if (seat_index != _seat_index) {
		//	table.log_error("DispatchCard 不是当前玩家操作");
		//	return false;
		//}
		// 是否已经响应
		if (playerStatus.is_respone()) {
			table.log_player_error(seat_index, "DispatchCard 出牌,玩家已操作");
			return true;
		}
		// 记录玩家的操作
		playerStatus.operate(operate_code, operate_card);
		playerStatus.clean_status();


		// 变量定义 优先级最高操作的玩家和操作--不通炮的算法
		int target_player = seat_index;
		int target_action = operate_code;
		int target_lou_code =luoCode;
		int target_p = 0;
		for (int p = 0; p < table.getTablePlayerNumber(); p++) {
			int i = (_seat_index + p) % table.getTablePlayerNumber();
			if (i == target_player) {
				target_p = table.getTablePlayerNumber() - p;
			}
		}
		int cbActionRank[] = new int[3];
		int cbMaxActionRand = 0;
		for (int p = 0; p < table.getTablePlayerNumber(); p++) {
			int i = (_seat_index + p) % table.getTablePlayerNumber();
			// 获取动作
			int cbUserActionRank = 0;
			// 优先级别
			int cbTargetActionRank = 0;
			if (table._playerStatus[i].has_action()) {
				if (table._playerStatus[i].is_respone()) {
					// 获取已经执行的动作的优先级
					cbUserActionRank = table._logic.get_action_rank(table._playerStatus[i].get_perform())
							+ table.getTablePlayerNumber() - p;
					cbActionRank[i] = cbUserActionRank;
				} else {
					// 获取最大的动作的优先级
					cbUserActionRank = table._logic.get_action_list_rank(table._playerStatus[i]._action_count,
							table._playerStatus[i]._action) + table.getTablePlayerNumber() - p;
				}

				if (table._playerStatus[target_player].is_respone()) {
					// 获取已经执行的动作的优先级
					cbTargetActionRank = table._logic.get_action_rank(table._playerStatus[target_player].get_perform())
							+ target_p;
					cbActionRank[i] = cbUserActionRank;
				} else {
					// 获取最大的动作的优先级
					cbTargetActionRank = table._logic.get_action_list_rank(
							table._playerStatus[target_player]._action_count,
							table._playerStatus[target_player]._action) + target_p;
				}

				// 优先级别
				// 动作判断 优先级最高的人和动作
				if (cbUserActionRank > cbTargetActionRank) {
					target_player = i;// 最高级别人
					target_action = table._playerStatus[i].get_perform();
					target_lou_code = table._playerStatus[i].get_lou_kind();
					target_p = table.getTablePlayerNumber() - p;
					cbMaxActionRand = cbUserActionRank;
				} 
			}
		}
	
			
		// 优先级最高的人还没操作
		
		if (table._playerStatus[target_player].is_respone() == false)
		{
			return true;
		}
	
		// 变量定义
		int target_card = table._playerStatus[target_player]._operate_card;
	
		for(int i = 0 ; i< table.getTablePlayerNumber();i++)
		{
			if(table._playerStatus[i].get_perform() == GameConstants.WIK_ZHE_WMQ)
			{
				table.operate_effect_action(i,GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[]{GameConstants.WIK_ZHE_WMQ}, 1,GameConstants.INVALID_SEAT);

				table.operate_zhe_zhe_count(i, true);
			}
		}

		// 用户状态
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			
			
			table._playerStatus[i].clean_action();
			table._playerStatus[i].clean_status();

			table.operate_player_action(i, true);
		}
		// 执行动作
		switch (target_action) {
		case GameConstants.WIK_ZHE_WMQ:
		case GameConstants.WIK_NULL:
		{
			// 用户状态
			table._playerStatus[_seat_index].clean_action();
			table._playerStatus[_seat_index].clean_status();
		
			if (table._playerStatus[_seat_index].lock_huan_zhang()) {
				GameSchedule.put(new OutCardRunnable(table.getRoom_id(), _seat_index, _send_card_data),
						GameConstants.DELAY_AUTO_OUT_CARD, TimeUnit.MILLISECONDS);
			} else {
				//要出牌，但是没有牌出设置成相公  下家用户发牌
				if(target_player == table._cur_banker)
        			table.GRR._cards_index[table._cur_banker][table._logic.switch_to_card_index(table._send_card_data)]--;
				table._provide_card = _send_card_data;// 提供的牌
				
				table.exe_chuli_first_card(_seat_index,GameConstants.WIK_NULL,300);
				_send_card_data = 0;
			}
			return true;

		}
		
		case GameConstants.WIK_ZI_MO: // 自摸
		{
			table.GRR._chi_hu_rights[target_player].set_valid(true);

			table.GRR._chi_hu_card[target_player][0] = operate_card;

			table._cur_banker = target_player;
//			if (table.has_rule(GameConstants.GAME_RULE_LIXIANG_FLS_ZHUANG)) {// 轮装
//				if (table.GRR._banker_player == target_player) {
//					table._cur_banker = target_player;
//				} else {
//					table._cur_banker = (table.GRR._banker_player + table.getTablePlayerNumber() + 1)
//							% table.getTablePlayerNumber();
//				}
//			}
			table._shang_zhuang_player = target_player;
			
			table.process_chi_hu_player_score_wmq_s(target_player, _seat_index, operate_card, true);
			table.process_chi_hu_player_operate(target_player, operate_card, true);
			// 记录
			if (table.GRR._chi_hu_rights[target_player].da_hu_count > 0) {
				table._player_result.da_hu_zi_mo[target_player]++;
			} else {
				table._player_result.xiao_hu_zi_mo[target_player]++;
			}
//			table.countChiHuTimes_wmq(target_player, true);

			int delay = 2;
		
			GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), target_player, GameConstants.Game_End_NORMAL),
					delay, TimeUnit.SECONDS);

			return true;
		}
		}

		return true;
	}

	@Override
	public boolean handler_player_be_in_room(WMQTable table, int seat_index) {
		super.handler_player_be_in_room(table, seat_index);
		table.istrustee[seat_index] = false;
		int ting_cards[] = table._playerStatus[seat_index]._hu_cards;
		int ting_count = table._playerStatus[seat_index]._hu_card_count;

		if (ting_count > 0) {
			table.operate_chi_hu_cards(seat_index, ting_count, ting_cards);
		}
		
	 return true;
	}
}
