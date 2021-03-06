package com.cai.game.mj.handler.xthh;

import java.util.concurrent.TimeUnit;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.domain.PlayerStatus;
import com.cai.common.domain.WeaveItem;
import com.cai.future.GameSchedule;
import com.cai.future.runnable.AddDiscardRunnable;
import com.cai.future.runnable.GameFinishRunnable;
import com.cai.game.mj.MJTable;
import com.cai.game.mj.handler.MJHandlerOutCardOperate;

import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.TableResponse;
import protobuf.clazz.Protocol.WeaveItemResponse;
import protobuf.clazz.Protocol.WeaveItemResponseArrayResponse;

public class MJHandlerOutCardOperate_XTHH extends MJHandlerOutCardOperate {
	@Override
	public void exe(MJTable table) {
		// TODO Auto-generated method stub
		PlayerStatus playerStatus = table._playerStatus[_out_card_player];
		
		// 重置玩家状态
		//playerStatus.clean_status();
		table.change_player_status(_out_card_player,GameConstants.INVALID_VALUE);
		playerStatus.clean_action();

		//
		// 出牌记录
		table._out_card_count++;
		table._out_card_player = _out_card_player;
		table._out_card_data = _out_card_data;
				
		//刷新手牌
		int cards[] = new int[GameConstants.MAX_COUNT];
		
		//刷新自己手牌
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[_out_card_player], cards);
		
		//癞子
		for(int j=0; j < hand_card_count; j++){
			if( table._logic.is_magic_card(cards[j])){
				cards[j]+=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
			}else if(table._logic.is_lai_gen_card(cards[j])){
				//cards[j]+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN;
			}
		}
		table.operate_player_cards(_out_card_player, hand_card_count, cards, 0, null);

		//癞子
		int real_card = _out_card_data;
		if(table._logic.is_magic_card(_out_card_data)){
			real_card+=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
		}
		//显示出牌
		table.operate_out_card(_out_card_player,1,new int[]{real_card},GameConstants.OUT_CARD_TYPE_MID,GameConstants.INVALID_SEAT);
		
		//检查听牌
//		table._playerStatus[_out_card_player]._hu_card_count = table.get_xthh_ting_card(table._playerStatus[_out_card_player]._hu_cards, table.GRR._cards_index[_out_card_player], 
//				table.GRR._weave_items[_out_card_player], table.GRR._weave_count[_out_card_player]);
//		
//		int ting_cards[] = table._playerStatus[_out_card_player]._hu_cards;
//		int ting_count = table._playerStatus[_out_card_player]._hu_card_count;
//		
//		
//		if(ting_count>0){
//			table.operate_chi_hu_cards(_out_card_player, ting_count, ting_cards);
//		}else{
//			ting_cards[0]=0;
//			table.operate_chi_hu_cards(_out_card_player, 1, ting_cards);
//		}
		
		//听的牌，保存，下次不用计算
		int ting_count = table._playerStatus[_out_card_player]._hu_out_card_count;
		int tc = 0;
		table._playerStatus[_out_card_player]._hu_card_count = 0;
		for(int i=0; i < ting_count; i++){
			int out_card = table._playerStatus[_out_card_player]._hu_out_card_ting[i];
			if(table._logic.is_magic_card(_out_card_data)){
				//打了癞子
				out_card-=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
			}
			if(out_card == _out_card_data){//
				tc = table._playerStatus[_out_card_player]._hu_card_count = table._playerStatus[_out_card_player]._hu_out_card_ting_count[i] ;
				for(int j=0; j <tc;j++){
					table._playerStatus[_out_card_player]._hu_cards[j] = table._playerStatus[_out_card_player]._hu_out_cards[i][j];
				}
			}
		}
		table._playerStatus[_out_card_player]._hu_out_card_count = 0;
		//删除听牌角标
		if(ting_count>0){
			table.operate_player_cards_flag(_out_card_player, 0, null, 0, null);
		}
		if(tc>0){
			int ting_cards[] = table._playerStatus[_out_card_player]._hu_cards;
			table.operate_chi_hu_cards(_out_card_player, tc,ting_cards);
		}else{
			table.operate_chi_hu_cards(_out_card_player, 1,new int[]{0});
		}
		
		
		table._provide_player = _out_card_player;
		table._provide_card = _out_card_data;

		boolean bAroseAction = false;
		boolean piao_lai = false;
		if(table._logic.is_magic_card(_out_card_data)==true){
			//飘赖
			table.GRR._piao_lai_seat[table.GRR._piao_lai_count]=_out_card_player;
			table.GRR._piao_lai_count++;
			
			piao_lai=true;
			
			table._player_result.piao_lai_count[_out_card_player]++;
			//飘赖效果
			//刷新倍数
			//int seat = table.refresh_bei_shu();
//			if(heijiahei){
//				table.GRR._especial_txt = "飘赖*"+table.GRR._piao_lai_count+"(黑加黑) 倍数:"+table.GRR._bei_shu;
//			}else{
//				table.GRR._especial_txt ="飘赖*"+table.GRR._piao_lai_count+" 倍数:"+table.GRR._bei_shu;
//			}
			table.GRR._especial_txt ="飘赖*"+table.GRR._piao_lai_count;//+" 倍数:"+table.GRR._bei_shu;
			
			table.GRR._especial_txt_type = GameConstants.Especial_Txt_Type_PIAO_LAI;
			
			//效果
			table.operate_effect_action(_out_card_player, GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[]{GameConstants.WIK_PIAO_LAI}, 1, GameConstants.INVALID_SEAT);
			
			if(table.has_rule(GameConstants.GAME_RULE_HEBEI_PIAO_LAI_YOU_JIANG)){
				//飘赖有奖
				int lai_count[] = new int[GameConstants.GAME_PLAYER];
				float score[] = new float[ GameConstants.GAME_PLAYER];

				for(int i=0; i < GameConstants.GAME_PLAYER; i++){
					score[i]=0;
					lai_count[i]=0;
				}
				
				for(int i=0; i < table.GRR._piao_lai_count; i++){
					lai_count[table.GRR._piao_lai_seat[i]]++;
				}
				
				for(int i=0; i < GameConstants.GAME_PLAYER; i++){
					float s=table._di_fen;
					if(i==_out_card_player){
						continue;
					}
					int count = lai_count[_out_card_player] + lai_count[i];
					if(count ==1 ){
						s *= 1;
					}else if(count ==2){
						s *= 2;
					}else if(count ==3){
						s *= 4;
					}else if(count ==4){
						s *= 8;
					}
					score[_out_card_player]+=s;
					score[i]-=s;
				}
				
				for(int i=0; i < GameConstants.GAME_PLAYER; i++){
					table.GRR._piao_lai_score[i] += score[i];
					//table.GRR._game_score[i] += score[i];
					table._player_result.game_score[i] += score[i];
				}
				
				//刷新玩家分数
				table.operate_player_score(_out_card_player, score);
			}
		
		}else{
			if(_type == GameConstants.WIK_MENG_XIAO ||
					_type == GameConstants.WIK_DIAN_XIAO||
					_type == GameConstants.WIK_HUI_TOU_XIAO||
					_type == GameConstants.WIK_XIAO_CHAO_TIAN||
					_type == GameConstants.WIK_DA_CHAO_TIAN){
				// 玩家出牌 响应判断,是否有吃碰杠补胡
				bAroseAction = table.estimate_player_out_card_respond_xthh(_out_card_player, _out_card_data,GameConstants.HU_CARD_TYPE_RE_CHONG);//, EstimatKind.EstimatKind_OutCard
			}else{
				bAroseAction = table.estimate_player_out_card_respond_xthh(_out_card_player, _out_card_data,GameConstants.HU_CARD_TYPE_PAOHU);//, EstimatKind.EstimatKind_OutCard
			}
			
		}

		// 如果没有需要操作的玩家，派发扑克
		if (bAroseAction == false) {
			for(int i=0; i < GameConstants.GAME_PLAYER; i++){
				table._playerStatus[i].clean_action();
				table.change_player_status(i,GameConstants.INVALID_VALUE);
				//table._playerStatus[i].clean_status();
			}
			
			table.operate_player_action(_out_card_player, true);
			
			//加入牌队
//			GameSchedule.put(new AddDiscardRunnable(table.getRoom_id(), _out_card_player,  1, new int[]{real_card}),
//					MJGameConstants.DELAY_ADD_CARD_DELAY, TimeUnit.MILLISECONDS);
			table.exe_add_discard( _out_card_player,  1, new int[]{real_card},false,GameConstants.DELAY_SEND_CARD_DELAY);
			if(piao_lai==true){
				table._current_player = _out_card_player;
				//发牌
				table.exe_dispatch_card(_out_card_player,GameConstants.WIK_PIAO_LAI,GameConstants.DELAY_SEND_CARD_DELAY);
			}else{
				// 用户切换
				int next_player =  (_out_card_player + GameConstants.GAME_PLAYER + 1) % GameConstants.GAME_PLAYER;
				table._current_player = next_player;
				//发牌
				table.exe_dispatch_card(next_player,GameConstants.WIK_NULL,GameConstants.DELAY_SEND_CARD_DELAY);
			}
			
			
		}else{
			//等待别人操作这张牌
			for (int i = 0; i < GameConstants.GAME_PLAYER; i++) {
				playerStatus = table._playerStatus[i];
				if(playerStatus.has_action()){
					table.change_player_status(i, GameConstants.Player_Status_OPR_CARD);
					if(playerStatus.has_chi_hu()){
						if(table.has_rule(GameConstants.GAME_RULE_HUNAN_JIANPAOHU)){
							//见炮胡
							table.exe_jian_pao_hu(i,GameConstants.WIK_CHI_HU,_out_card_data);
						}else{
							table.operate_player_action(i, false);
						}
					}else{
						table.operate_player_action(i, false);
					}
					
				}
				handler_check_auto_behaviour(table,i,_out_card_data);
			}
		} 
	}
	
	/***
	 * //用户操作
	 * 
	 * @param seat_index
	 * @param operate_code
	 * @param operate_card
	 * @return
	 */
	@Override
	public boolean handler_operate_card(MJTable table,int seat_index, int operate_code, int operate_card){
		// 效验状态
		
		PlayerStatus playerStatus = table._playerStatus[seat_index];
		
		 // 是否已经响应
		if(playerStatus.has_action()==false){
			table.log_player_error(seat_index, "出牌,玩家操作已失效");
			return true;
		}
		
		 // 是否已经响应
		if(playerStatus.is_respone()){
			table.log_player_error(seat_index, "出牌,玩家已操作");
			return true;
		}
		
		if ((operate_code != GameConstants.WIK_NULL) && playerStatus.has_action_by_code(operate_code) == false)// 没有这个操作动作
		{
			table.log_player_error(seat_index, "出牌操作,没有动作");
			return true;
		}

		

		//玩家的操作
		playerStatus.operate(operate_code,operate_card);
		
		if(operate_code == GameConstants.WIK_CHI_HU){
			table.GRR._chi_hu_rights[seat_index].set_valid(true);//胡牌生效
			//效果
			table.process_chi_hu_player_operate_xthh(seat_index, operate_card,false);
			
		}else if(operate_code == GameConstants.WIK_NULL){
			table.record_discard_gang(seat_index);
			table.record_effect_action(seat_index,GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[]{GameConstants.WIK_NULL}, 1);
			
			if(table._playerStatus[seat_index].has_chi_hu()){
				table._playerStatus[seat_index].chi_hu_round_invalid();//这一轮就不能吃胡了没过牌之前都不能胡
			}
		}
		//执行操作的玩家没有被托管  已经响应    取消托管倒计时
		if( (!table.istrustee[seat_index] )&& table._trustee_schedule[seat_index]!=null){
			table._trustee_schedule[seat_index].cancel(false);
			table._trustee_schedule[seat_index] = null;
		}
		
		// 吃胡等待 因为胡牌的等级是一样的，可以一炮多响，看看是不是还有能胡的
		for (int i = 0; i < GameConstants.GAME_PLAYER; i++) {
			if ((table._playerStatus[i].is_respone()== false) && (table._playerStatus[i].has_chi_hu()))
				return false;
		}
		
		// 变量定义 优先级最高操作的玩家和操作
		int target_player = seat_index;
		int target_action = operate_code;
		
		
		// 执行判断
		for (int p = 0; p <GameConstants.GAME_PLAYER; p++){
			int i =(_out_card_player+p) % GameConstants.GAME_PLAYER;
			// 获取动作
			int cbUserActionRank =0;
			
			if(table._playerStatus[i].has_action()){
				if(table._playerStatus[i].is_respone()){
					//获取已经执行的动作的优先级
					cbUserActionRank =  table._logic.get_action_rank(table._playerStatus[i].get_perform());
				}else{
					//获取最大的动作的优先级
					cbUserActionRank =  table._logic.get_action_list_rank(table._playerStatus[i]._action_count,table._playerStatus[i]._action);
				}
				
				// 优先级别
				int cbTargetActionRank = table._logic.get_action_rank(table._playerStatus[target_player].get_perform());

				// 动作判断 优先级最高的人和动作
				if (cbUserActionRank > cbTargetActionRank) {
					target_player = i;//最高级别人
					target_action = table._playerStatus[i].get_perform();
				}
			}
		}
		// 优先级最高的人还没操作
		if (table._playerStatus[target_player].is_respone()== false)
			return true;

		// 变量定义
		int target_card = table._playerStatus[target_player]._operate_card;

		// 用户状态
		for (int i = 0; i < GameConstants.GAME_PLAYER; i++) {
			table._playerStatus[i].clean_action();
			table.change_player_status(i,GameConstants.INVALID_VALUE);
			//table._playerStatus[i].clean_status();
			
			table.operate_player_action(i, true);
		}
		
		// 删除扑克
		switch (target_action) {
			case GameConstants.WIK_LEFT: // 上牌操作
				{
					// 删除扑克
					int cbRemoveCard[] = new int[] { target_card + 1, target_card + 2 };
					if (!table._logic.remove_cards_by_index(table.GRR._cards_index[target_player], cbRemoveCard, 2)) {
						table.log_player_error(seat_index, "吃牌删除出错");
						return false;
					}
					table.exe_chi_peng( target_player, _out_card_player,target_action, target_card,GameConstants.CHI_PENG_TYPE_OUT_CARD);
				}
				break;
			case GameConstants.WIK_RIGHT: // 上牌操作
				{
					// 删除扑克
					int cbRemoveCard[] = new int[] { target_card - 1, target_card - 2 };
		
					if (!table._logic.remove_cards_by_index(table.GRR._cards_index[target_player], cbRemoveCard, 2)) {
						table.log_player_error(seat_index, "吃牌删除出错");
						return false;
					}
					table.exe_chi_peng( target_player, _out_card_player,target_action, target_card,GameConstants.CHI_PENG_TYPE_OUT_CARD);
				}
				break;
			case GameConstants.WIK_CENTER: // 上牌操作
				{
					// 删除扑克
					int cbRemoveCard[] = new int[] { target_card - 1, target_card + 1 };
					if (!table._logic.remove_cards_by_index(table.GRR._cards_index[target_player], cbRemoveCard, 2)) {
						table.log_player_error(seat_index, "吃牌删除出错");
						return false;
					}
					table.exe_chi_peng( target_player, _out_card_player,target_action, target_card,GameConstants.CHI_PENG_TYPE_OUT_CARD);
				}
				break;
			case GameConstants.WIK_PENG: // 碰牌操作
				{
					// 删除扑克
					int cbRemoveCard[] = new int[] { target_card, target_card };
					if (!table._logic.remove_cards_by_index(table.GRR._cards_index[target_player], cbRemoveCard, 2)) {
						table.log_player_error(seat_index, "碰牌删除出错");
						return false;
					}
		
					table.exe_chi_peng( target_player, _out_card_player,target_action, target_card,GameConstants.CHI_PENG_TYPE_OUT_CARD);
				}
				
				break;
			case GameConstants.WIK_DIAN_XIAO: // 杠牌操作
			{
				//是否有抢杠胡
				table.exe_gang(target_player, _out_card_player, target_card, target_action, GameConstants.WIK_DIAN_XIAO, false,false);
				return true;
			}
			case GameConstants.WIK_XIAO_CHAO_TIAN: // 小朝天 能不能抢杠胡？
			{
				//是否有抢杠胡
				table.exe_gang(target_player, _out_card_player, target_card, target_action, GameConstants.WIK_XIAO_CHAO_TIAN, false,false);
				return true;
			}
			case  GameConstants.WIK_NULL:{

				//删掉出来的那张牌
				//table.operate_out_card(this._out_card_player, 0, null,MJGameConstants.OUT_CARD_TYPE_MID,MJGameConstants.INVALID_SEAT);
				int real_card = _out_card_data;
				if(table._logic.is_magic_card(_out_card_data)){
					real_card+=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
				}
				//加到牌堆 没有人要  
				table.exe_add_discard(this._out_card_player, 1, new int[]{real_card},false,0);
				
				// 用户切换
				_current_player = table._current_player = (_out_card_player + GameConstants.GAME_PLAYER + 1) % GameConstants.GAME_PLAYER;
				// 发牌
				//dispatch_card_data(_resume_player, false);
				
				//发牌
				table.exe_dispatch_card(_current_player,GameConstants.WIK_NULL,0);
				
				return true;
			}
			case GameConstants.WIK_CHI_HU: //胡
			{
				int jie_pao_count = 0;
				for(int i=0; i<GameConstants.GAME_PLAYER;i++){
					if((i == _out_card_player) ||(table.GRR._chi_hu_rights[i].is_valid()==false )){
						continue;
					}
					jie_pao_count++;
				}
				
				if(jie_pao_count>0){
					if(jie_pao_count>1){
						//结束
						table._banker_select = _out_card_player;
						// 结束后设置鸟牌
					//	table.set_niao_card(_out_card_player,MJGameConstants.INVALID_VALUE,true,0);
					}else if(jie_pao_count==1){
						table._banker_select = seat_index;
						// 结束后设置鸟牌 
					//	table.set_niao_card(seat_index,MJGameConstants.INVALID_VALUE,true,0);
					}
					for(int i=0; i<GameConstants.GAME_PLAYER;i++){
						if((i == _out_card_player) ||(table.GRR._chi_hu_rights[i].is_valid()==false )){
							continue;
						}
						table.process_chi_hu_player_score_xthh(i,_out_card_player, _out_card_data,false);
						table.GRR._chi_hu_card[i][0] = _out_card_data;
						// 记录
						table._player_result.jie_pao_count[i]++;
						table._player_result.dian_pao_count[_out_card_player]++;
					}
					
					GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), table._banker_select, GameConstants.Game_End_NORMAL),
							GameConstants.GAME_FINISH_DELAY, TimeUnit.SECONDS);

				}
				return true;
			}
			default:
				return false;
		}
		return true;			
	}
	
//	@Override
//	protected void exe_chi_peng(MJTable table,int target_player,int target_action,int target_card){
//		// 组合扑克
//		int wIndex = table.GRR._weave_count[target_player]++;
//		table.GRR._weave_items[target_player][wIndex].public_card = 1;
//		table.GRR._weave_items[target_player][wIndex].center_card = target_card;
//		table.GRR._weave_items[target_player][wIndex].weave_kind = target_action;
//		table.GRR._weave_items[target_player][wIndex].provide_player = _out_card_player;
//		
//		//删掉出来的那张牌
//		//table.operate_out_card(this._out_card_player, 0, null,MJGameConstants.OUT_CARD_TYPE_MID,MJGameConstants.INVALID_SEAT);
//		table.operate_remove_discard(this._out_card_player, table.GRR._discard_count[_out_card_player]);
//		// 设置用户
//		_current_player = table._current_player = target_player;
//
//		
//		//效果
//		table.operate_effect_action(target_player,MJGameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[]{target_action}, 1,MJGameConstants.INVALID_SEAT);
//		
//		//刷新手牌包括组合
//		int cards[]= new int[MJGameConstants.MAX_COUNT];
//		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[target_player], cards);
//		for(int j=0; j < hand_card_count; j++){
//			if( table._logic.is_magic_card(cards[j])){
//				cards[j]+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
//			}
//		}
//		WeaveItem weaves[] = new WeaveItem[MJGameConstants.MAX_WEAVE];
//		int weave_count = table.GRR._weave_count[target_player];
//		for (int i = 0; i < weave_count; i++) {
//			weaves[i]= new WeaveItem();
//			weaves[i].weave_kind = table.GRR._weave_items[target_player][i].weave_kind;
//			weaves[i].center_card = table.GRR._weave_items[target_player][i].center_card;
//			weaves[i].public_card = table.GRR._weave_items[target_player][i].public_card;
//			weaves[i].provide_player = table.GRR._weave_items[target_player][i].provide_player+MJGameConstants.WEAVE_SHOW_DIRECT;
//			//癞子
//			if(table._logic.is_magic_card(weaves[i].center_card)){
//				weaves[i].center_card+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
//			}else if(table._logic.is_lai_gen_card(weaves[i].center_card)){
//				//weaves[i].center_card+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN;
//			}
//		}
//		table.operate_player_cards(target_player, hand_card_count, cards, weave_count, weaves);
//		
//		table.exe_chi_peng(target_player,target_action);
//		
//	}
	
	@Override
	public boolean handler_player_be_in_room(MJTable table,int seat_index) {
		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_RECONNECT_DATA);

		TableResponse.Builder tableResponse = TableResponse.newBuilder();

		table.load_room_info_data(roomResponse);
		table.load_player_info_data(roomResponse);
		table.load_common_status(roomResponse);
		
		
		// 游戏变量
		tableResponse.setBankerPlayer(table.GRR._banker_player);
		tableResponse.setCurrentPlayer(_out_card_player);
		tableResponse.setCellScore(0);

		// 状态变量
		tableResponse.setActionCard(0);
		//tableResponse.setActionMask((_response[seat_index] == false) ? _player_action[seat_index] : MJGameConstants.WIK_NULL);

		// 历史记录
		tableResponse.setOutCardData(0);
		tableResponse.setOutCardPlayer(0);

		for (int i = 0; i < GameConstants.GAME_PLAYER; i++) {
			tableResponse.addTrustee(false);// 是否托管
			// 剩余牌数
			tableResponse.addDiscardCount(table.GRR._discard_count[i]);
			Int32ArrayResponse.Builder int_array = Int32ArrayResponse.newBuilder();
			for (int j = 0; j < 55; j++) {
				if(table._logic.is_magic_card(table.GRR._discard_cards[i][j])){
					//癞子
					int_array.addItem(table.GRR._discard_cards[i][j]+GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI);
				}else{
					int_array.addItem(table.GRR._discard_cards[i][j]);
				}
			}
			tableResponse.addDiscardCards(int_array);

			// 组合扑克
			tableResponse.addWeaveCount(table.GRR._weave_count[i]);
			WeaveItemResponseArrayResponse.Builder weaveItem_array = WeaveItemResponseArrayResponse.newBuilder();
			for (int j = 0; j < GameConstants.MAX_WEAVE; j++) {
				WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
				if(table._logic.is_magic_card(table.GRR._weave_items[i][j].center_card)){
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card+GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI);
				}else if(table._logic.is_lai_gen_card(table.GRR._weave_items[i][j].center_card)){
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);//weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card+MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN);
				}else{
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);
				}
				weaveItem_item.setProvidePlayer(table.GRR._weave_items[i][j].provide_player);
				weaveItem_item.setPublicCard(table.GRR._weave_items[i][j].public_card);
				weaveItem_item.setWeaveKind(table.GRR._weave_items[i][j].weave_kind);
				weaveItem_array.addWeaveItem(weaveItem_item);
			}
			tableResponse.addWeaveItemArray(weaveItem_array);

			//
			tableResponse.addWinnerOrder(0);

			// 牌
			tableResponse.addCardCount(table._logic.get_card_count_by_index(table.GRR._cards_index[i]));
		}

		// 数据
		tableResponse.setSendCardData(0);
		int cards[] = new int[GameConstants.MAX_COUNT];
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[seat_index], cards);
		for(int j=0; j < hand_card_count; j++){
			if( table._logic.is_magic_card(cards[j])){
				cards[j]+=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
			}else if(table._logic.is_lai_gen_card(cards[j])){
				//cards[j]+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN;
			}
		}
		for (int i = 0; i < GameConstants.MAX_COUNT; i++) {
			tableResponse.addCardsData(cards[i]);
		}
		
		table.load_out_card_ting(seat_index,roomResponse,tableResponse);
		roomResponse.setTable(tableResponse);
		table.send_response_to_player(seat_index, roomResponse);
		
		int real_card = _out_card_data;
		if(table._logic.is_magic_card(_out_card_data)){
			real_card+=GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
		}
		//出牌
		table.operate_out_card(_out_card_player, 1, new int[]{real_card},GameConstants.OUT_CARD_TYPE_MID,seat_index);
		
		//table.operate_player_get_card(_seat_index, 1, new int[]{_send_card_data});
		if(table._playerStatus[seat_index].has_action()&& (table._playerStatus[seat_index].is_respone()==false)){
			table.operate_player_action(seat_index, false);
		}
		table.be_in_room_trustee(seat_index);
		//听牌显示
//		int ting_cards[] = table._playerStatus[seat_index]._hu_cards;
//		int ting_count = table._playerStatus[seat_index]._hu_card_count;
//		
//		if(ting_count>0){
//			table.operate_chi_hu_cards(seat_index, ting_count, ting_cards);
//		}
		
		return true;
	}
	
	@Override
	public boolean handler_be_set_trustee(MJTable table, int seat_index){
		handler_check_auto_behaviour(table,seat_index,_out_card_data);
		return false;
	}
}
