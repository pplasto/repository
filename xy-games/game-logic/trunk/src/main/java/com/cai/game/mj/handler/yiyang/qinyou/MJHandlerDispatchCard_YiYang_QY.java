package com.cai.game.mj.handler.yiyang.qinyou;

import java.util.concurrent.TimeUnit;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.constant.game.GameConstants_YiYang_QY;
import com.cai.common.domain.ChiHuRight;
import com.cai.common.domain.PlayerStatus;
import com.cai.common.util.GameDescUtil;
import com.cai.future.GameSchedule;
import com.cai.future.runnable.GameFinishRunnable;
import com.cai.future.runnable.OutCardRunnable;
import com.cai.game.mj.handler.MJHandlerDispatchCard;

import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.TableResponse;
import protobuf.clazz.Protocol.WeaveItemResponse;
import protobuf.clazz.Protocol.WeaveItemResponseArrayResponse;

public class MJHandlerDispatchCard_YiYang_QY extends MJHandlerDispatchCard<MJTable_YiYang_QY> {
	boolean ting_send_card = false;

	@SuppressWarnings("static-access")
	@Override
	public void exe(MJTable_YiYang_QY table) {
		table.player_dispatch_count[_seat_index]++;
		// 用户状态--清除状态
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			table._playerStatus[i].clean_action();
			table.change_player_status(i, GameConstants.INVALID_VALUE);
		}
		ting_send_card = false;
		table._playerStatus[_seat_index].chi_hu_round_valid();// 可以胡了

		int llcard = 0;
		// 荒庄结束
		if (table.GRR._left_card_count == 0) {
			for (int i = 0; i < table.getTablePlayerNumber(); i++) {
				table.GRR._chi_hu_card[i][0] = GameConstants.INVALID_VALUE;
			}
			table._cur_banker = table.GRR._banker_player;
			// 流局
			table.handler_game_finish(table._cur_banker, GameConstants.Game_End_DRAW);

			return;
		} else if (table.GRR._left_card_count == 1) {
			table._send_card_count++;
			_send_card_data = table._repertory_card[table._all_card_len - table.GRR._left_card_count];

			--table.GRR._left_card_count;
			
			if (table.DEBUG_CARDS_MODE) {
				_send_card_data = 0x22;
			}
			//客户端显示到桌面
			table.operate_out_card(_seat_index, 1, new int[]{_send_card_data},
	                GameConstants.OUT_CARD_TYPE_LEFT, GameConstants.INVALID_SEAT);
			boolean has_hu = false;
			for (int i = 0; i < table.getTablePlayerNumber(); i++) {
				
				ChiHuRight chr = table.GRR._chi_hu_rights[i];
				chr.set_empty();

				int card_type = GameConstants.HU_CARD_TYPE_ZIMO;
				int action = table.analyse_chi_hu_card(table.GRR._cards_index[i], table.GRR._weave_items[i],
						table.GRR._weave_count[i], _send_card_data, chr, card_type, i);// 自摸

				if (action != GameConstants.WIK_NULL) {
					has_hu = true;
					// 添加动作
					table._playerStatus[i].add_action(GameConstants.WIK_ZI_MO);
					table._playerStatus[i].add_zi_mo(_send_card_data, i);
					chr.opr_or(GameConstants.CHR_HUNAN_HAI_DI_LAO);// 海底胡
				} else {
					chr.set_empty();
				}
			}
			if(has_hu){
				for (int i = 0; i < table.getTablePlayerNumber(); i++) {
					if (table._playerStatus[i].has_action()) {
						table.change_player_status(i, GameConstants.Player_Status_OPR_CARD);
						table.operate_player_action(i, false);
					}
				}
			}else{
				GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), _seat_index, GameConstants.Game_End_DRAW), 1500,
						TimeUnit.MILLISECONDS);
			}
			return;
		}

		PlayerStatus curPlayerStatus = table._playerStatus[_seat_index];
		curPlayerStatus.reset();

		table._current_player = _seat_index;// 轮到操作的人是自己

		// 从牌堆拿出一张牌
		table._send_card_count++;
		_send_card_data = table._repertory_card[table._all_card_len - table.GRR._left_card_count];

		--table.GRR._left_card_count;
		if (table.GRR._left_card_count == llcard) {
			table.GRR._banker_player = _seat_index;
		}

		table._provide_player = _seat_index;

		if (table.DEBUG_CARDS_MODE) {
			_send_card_data = 0x22;
		}

		// TODO 统计玩家抓的牌
		table.zhua_pai_count[_seat_index]++;

		// 发牌处理,判断发给的这个人有没有胡牌或杠牌
		// 胡牌判断
		ChiHuRight chr = table.GRR._chi_hu_rights[_seat_index];
		chr.set_empty();

		int card_type = GameConstants.HU_CARD_TYPE_ZIMO;
		if (_type == GameConstants.HU_CARD_TYPE_GANG_KAI) {
			card_type = GameConstants.HU_CARD_TYPE_GANG_KAI;
		}
		int action = table.analyse_chi_hu_card(table.GRR._cards_index[_seat_index], table.GRR._weave_items[_seat_index],
				table.GRR._weave_count[_seat_index], _send_card_data, chr, card_type, _seat_index);// 自摸

		if (action != GameConstants.WIK_NULL) {
			// 添加动作
			curPlayerStatus.add_action(GameConstants.WIK_ZI_MO);
			curPlayerStatus.add_zi_mo(_send_card_data, _seat_index);
			chr.opr_or(GameConstants.CHR_ZI_MO);
		} else {
			chr.set_empty();
		}
		
		if (GameDescUtil.has_rule(table.getGameRuleIndexEx(), GameConstants_YiYang_QY.GAME_RULE_HUNAN_BAO_TING)
				&& (_type == GameConstants.WIK_NULL || _type == GameConstants.DispatchCard_Type_Tian_Hu)) {
			// 能不能报听
			if (!table._playerStatus[_seat_index].is_bao_ting() && table.GRR._weave_count[_seat_index] == 0
					&& table.zhua_pai_count[_seat_index] == 1) {
				int send_card_index = table._logic.switch_to_card_index(_send_card_data);
				// 加到手牌
				table.GRR._cards_index[_seat_index][send_card_index]++;
				int count = 0;
				int ting_count = 0;

				for (int i = 0; i < GameConstants.MAX_INDEX - GameConstants.CARD_HUA_COUNT - GameConstants.CARD_FENG_COUNT; i++) {
					count = table.GRR._cards_index[_seat_index][i];
					if (count > 0) {
						// if ((send_card_index == i) && (count == 1)) {
						// // 摸起来的牌只有一张。不需判断
						// continue;
						// }
						// 假如打出这张牌
						table.GRR._cards_index[_seat_index][i]--;

						// 检查打出哪一张牌后可以听牌
						table._playerStatus[_seat_index]._hu_out_card_ting_count[ting_count] = table.get_ting_card(
								table._playerStatus[_seat_index]._hu_out_cards[ting_count], table.GRR._cards_index[_seat_index],
								table.GRR._weave_items[_seat_index], table.GRR._weave_count[_seat_index], _seat_index);

						if (table._playerStatus[_seat_index]._hu_out_card_ting_count[ting_count] > 0) {
							table._playerStatus[_seat_index]._hu_out_card_ting[ting_count] = table._logic.switch_to_card_data(i);

							// 能听牌
							ting_count++;
							if (i == send_card_index)
								ting_send_card = true;
						}

						// 加回来
						table.GRR._cards_index[_seat_index][i]++;
					}
				}
				// //减掉发的牌
				table.GRR._cards_index[_seat_index][send_card_index]--;

				// 如果可以报听,刷新自己的手牌
				table._playerStatus[_seat_index]._hu_out_card_count = ting_count;
				if (ting_count > 0) {
					// 刷新手牌
					int cards[] = new int[GameConstants.MAX_COUNT];
					// 刷新自己手牌
					int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[_seat_index], cards);

					for (int i = 0; i < hand_card_count; i++) {
						for (int j = 0; j < ting_count; j++) {
							if (table._logic.is_magic_card(cards[i])) {
								cards[i] += GameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
							} else {
								if (cards[i] == table._playerStatus[_seat_index]._hu_out_card_ting[j]) {
									cards[i] += GameConstants.CARD_ESPECIAL_TYPE_TING;
								}
							}
						}
					}

					table.operate_player_cards_with_ting(_seat_index, hand_card_count, cards, 0, null);
					// 添加动作
					curPlayerStatus.add_action(GameConstants.WIK_BAO_TING);
				}
			}
		}

		// 加到手牌
		table.GRR._cards_index[_seat_index][table._logic.switch_to_card_index(_send_card_data)]++;

		// 癞子
		int real_card = _send_card_data;

		if (ting_send_card) {
			real_card += GameConstants.CARD_ESPECIAL_TYPE_TING;
		} else if (table._logic.is_magic_card(_send_card_data)) {
			real_card += GameConstants.CARD_ESPECIAL_TYPE_HUN;
		}
		// 发送数据
		// 只有自己才有数值
		table.operate_player_get_card(_seat_index, 1, new int[] { real_card }, GameConstants.INVALID_SEAT);

		// 设置变量
		table._provide_card = _send_card_data;// 提供的牌
		table._send_card_data = _send_card_data;// 提供的牌
		m_gangCardResult.cbCardCount = 0;
		if (table.GRR._left_card_count > 1) {
			int cbActionMask = table.analyse_gang(table.GRR._cards_index[_seat_index], table.GRR._weave_items[_seat_index],
					table.GRR._weave_count[_seat_index], m_gangCardResult, _send_card_data);

			if (cbActionMask != GameConstants.WIK_NULL) {
				for (int i = 0; i < m_gangCardResult.cbCardCount; i++) {
					if(!table._playerStatus[_seat_index].is_bao_ting() ||
							m_gangCardResult.isPublic[i] == 0){
						if(table.is_can_gang(_seat_index, m_gangCardResult.cbCardData[i])){
							if(!curPlayerStatus.has_action_by_code(GameConstants.WIK_GANG))
								curPlayerStatus.add_action(GameConstants.WIK_GANG);
							curPlayerStatus.add_gang(m_gangCardResult.cbCardData[i], _seat_index, m_gangCardResult.isPublic[i]);
						}
					}
				}
			}
		}

		if (curPlayerStatus.has_action()) {// 有动作
			// 操作状态
			table.change_player_status(_seat_index, GameConstants.Player_Status_OPR_CARD);
			table.operate_player_action(_seat_index, false);
		} else {
			// 不能换章,自动出牌
			if (table._playerStatus[_seat_index].is_bao_ting()) {
				GameSchedule.put(new OutCardRunnable(table.getRoom_id(), _seat_index, _send_card_data), GameConstants.DELAY_AUTO_OUT_CARD,
						TimeUnit.MILLISECONDS);
			} else {
				table.change_player_status(_seat_index, GameConstants.Player_Status_OUT_CARD);
				table.operate_player_status();
			}
		}

		return;
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
	public boolean handler_operate_card(MJTable_YiYang_QY table, int seat_index, int operate_code, int operate_card) {
		PlayerStatus playerStatus = table._playerStatus[seat_index];
		if(table.GRR._left_card_count == 0){
			operate_card = _send_card_data;
		}
		// 效验操作
		if ((operate_code != GameConstants.WIK_NULL) && (playerStatus.has_action_by_code(operate_code) == false)) {
			table.log_error("没有这个操作");
			return false;
		}
		if(table.GRR._left_card_count != 0){
			if (seat_index != _seat_index) {
				table.log_error("不是当前玩家操作");
				return false;
			}
		}

		// 是否已经响应
		if (playerStatus.is_respone()) {
			table.log_player_error(seat_index, "出牌,玩家已操作");
			return true;
		}

		// 记录玩家的操作
		playerStatus.operate(operate_code, operate_card);
		table.change_player_status(seat_index, GameConstants.INVALID_VALUE);
		
		
		//////
		int target_player = seat_index;
        int target_action = operate_code;
        int target_p = 0;
        if(table.GRR._left_card_count == 0){
        	for (int p = 0; p < table.getTablePlayerNumber(); p++) {
        		int i = (_seat_index + p) % table.getTablePlayerNumber();
        		if (i == target_player) {
        			target_p = table.getTablePlayerNumber() - p;
        		}
        	}
        	for (int p = 0; p < table.getTablePlayerNumber(); p++) {
        		int i = (_seat_index + p) % table.getTablePlayerNumber();
        		int cbUserActionRank = 0;
        		int cbTargetActionRank = 0;
        		if (table._playerStatus[i].has_action()) {
        			if (table._playerStatus[i].is_respone()) {
        				cbUserActionRank = table._logic.get_action_rank(table._playerStatus[i].get_perform())
        						+ table.getTablePlayerNumber() - p;
        			} else {
        				cbUserActionRank = table._logic.get_action_list_rank(table._playerStatus[i]._action_count,
        						table._playerStatus[i]._action) + table.getTablePlayerNumber() - p;
        			}
        			
        			if (table._playerStatus[target_player].is_respone()) {
        				cbTargetActionRank = table._logic.get_action_rank(table._playerStatus[target_player].get_perform())
        						+ target_p;
        			} else {
        				cbTargetActionRank = table._logic.get_action_list_rank(
        						table._playerStatus[target_player]._action_count,
        						table._playerStatus[target_player]._action) + target_p;
        			}
        			
        			if (cbUserActionRank > cbTargetActionRank) {
        				target_player = i;
        				target_action = table._playerStatus[i].get_perform();
        				target_p = table.getTablePlayerNumber() - p;
        			}
        		}
        	}
        	
        	if (table._playerStatus[target_player].is_respone() == false)
        		return true;
        }
		
		
		////////

		// 放弃操作
		if (target_action == GameConstants.WIK_NULL) {
			table.record_effect_action(seat_index, GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[] { GameConstants.WIK_NULL }, 1);

			// 用户状态
			table._playerStatus[_seat_index].clean_action();
			table.change_player_status(_seat_index, GameConstants.INVALID_VALUE);
			
			if(table.GRR._left_card_count == 0){
				GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), _seat_index, GameConstants.Game_End_DRAW), 1500,
						TimeUnit.MILLISECONDS);
				return true;
			}
			// 不能换章,自动出牌
			if (table._playerStatus[_seat_index].is_bao_ting()) {
				GameSchedule.put(new OutCardRunnable(table.getRoom_id(), _seat_index, _send_card_data), GameConstants.DELAY_AUTO_OUT_CARD,
						TimeUnit.MILLISECONDS);
			} else {
				table.change_player_status(_seat_index, GameConstants.Player_Status_OUT_CARD);
				table.operate_player_status();
			}

			return true;
		}
		
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			table._playerStatus[i].clean_action();
			// table._playerStatus[i].clean_status();
			table.change_player_status(i, GameConstants.INVALID_VALUE);
			table.operate_player_action(i, true);
		}

		// 执行动作
		switch (target_action) {
		case GameConstants.WIK_GANG: // 杠牌操作
		{
			for (int i = 0; i < m_gangCardResult.cbCardCount; i++) {
				if (operate_card == m_gangCardResult.cbCardData[i]) {
					// 是否有抢杠胡
					table.exe_gang(_seat_index, _seat_index, operate_card, operate_code, m_gangCardResult.type[i], true, false);
					return true;
				}
			}
		}
			break;
		case GameConstants.WIK_ZI_MO: // 自摸
		{
			table.GRR._chi_hu_rights[target_player].set_valid(true);

			if (table._out_card_count == 0) {// 天胡
				ChiHuRight chr = table.GRR._chi_hu_rights[target_player];
				chr.opr_or(GameConstants.CHR_HUNAN_TIAN_HU);
			}


			table.GRR._chi_hu_card[target_player][0] = operate_card;

			table._cur_banker = target_player;
			table.process_chi_hu_player_operate(target_player, operate_card, table.GRR._left_card_count != 0);
			table.set_niao_card(_seat_index, GameConstants.INVALID_VALUE, true, 0);// 抓鸟
			table.process_chi_hu_player_score(target_player, target_player, operate_card, true);

			// 记录
			table._player_result.zi_mo_count[target_player]++;

			// 记录
			if (table.GRR._chi_hu_rights[target_player].da_hu_count > 0) {
				table._player_result.da_hu_zi_mo[target_player]++;
			} else {
				table._player_result.xiao_hu_zi_mo[target_player]++;
			}

			GameSchedule.put(new GameFinishRunnable(table.getRoom_id(), target_player, GameConstants.Game_End_NORMAL), GameConstants.GAME_FINISH_DELAY,
					TimeUnit.SECONDS);

			return true;
		}
		case GameConstants.WIK_BAO_TING: // 报听
		{
			operate_card -= GameConstants.CARD_ESPECIAL_TYPE_TING;

			ChiHuRight chr = table.GRR._chi_hu_rights[_seat_index];
			chr.qi_shou_bao_ting = GameConstants.CHR_HUNAN_QISHOU_BAO_TING;

			if (table._logic.is_valid_card(operate_card) == false) {
				table.log_error("出牌,牌型出错");
				return false;
			}

			// 效验参数
			if (seat_index != _seat_index) {
				table.log_error("出牌,没到出牌");
				return false;
			}

			// 删除扑克
			if (table._logic.remove_card_by_index(table.GRR._cards_index[_seat_index], operate_card) == false) {
				table.log_error("出牌删除出错");
				return false;
			}

			// 报听
			table.exe_out_card_bao_ting(_seat_index, operate_card,
					_type == GameConstants.DispatchCard_Type_Tian_Hu ? GameConstants.OutCard_Type_Di_Hu : GameConstants.WIK_NULL);
			return true;
		}
		}

		return true;
	}

	@Override
	public boolean handler_player_be_in_room(MJTable_YiYang_QY table, int seat_index) {
		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_RECONNECT_DATA);

		TableResponse.Builder tableResponse = TableResponse.newBuilder();

		table.load_room_info_data(roomResponse);
		table.load_player_info_data(roomResponse);
		table.load_common_status(roomResponse);

		// 游戏变量
		tableResponse.setBankerPlayer(table.GRR._banker_player);
		tableResponse.setCurrentPlayer(_seat_index);
		tableResponse.setCellScore(0);

		// 状态变量
		tableResponse.setActionCard(0);
		// tableResponse.setActionMask((_response[seat_index] == false) ?
		// _player_action[seat_index] : MJGameConstants.WIK_NULL);

		// 历史记录
		tableResponse.setOutCardData(0);
		tableResponse.setOutCardPlayer(0);

		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			tableResponse.addTrustee(false);// 是否托管
			// 剩余牌数
			tableResponse.addDiscardCount(table.GRR._discard_count[i]);
			Int32ArrayResponse.Builder int_array = Int32ArrayResponse.newBuilder();
			for (int j = 0; j < 55; j++) {
				if (j == table.GRR._chi_hu_rights[i].bao_ting_index) {
					int_array.addItem(table.GRR._discard_cards[i][j] + GameConstants.CARD_ESPECIAL_TYPE_BAO_TING);
				} else {
					int_array.addItem(table.GRR._discard_cards[i][j]);
				}
			}
			tableResponse.addDiscardCards(int_array);
			// 组合扑克
			tableResponse.addWeaveCount(table.GRR._weave_count[i]);
			WeaveItemResponseArrayResponse.Builder weaveItem_array = WeaveItemResponseArrayResponse.newBuilder();
			for (int j = 0; j < GameConstants.MAX_WEAVE; j++) {
				WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
				weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);
				weaveItem_item.setProvidePlayer(table.GRR._weave_items[i][j].provide_player + GameConstants.WEAVE_SHOW_DIRECT);
				weaveItem_item.setPublicCard(table.GRR._weave_items[i][j].public_card);
				weaveItem_item.setWeaveKind(table.GRR._weave_items[i][j].weave_kind);
				weaveItem_array.addWeaveItem(weaveItem_item);
			}
			tableResponse.addWeaveItemArray(weaveItem_array);

			//
			tableResponse.addWinnerOrder(0);

			// 牌

			if (i == _seat_index) {
				tableResponse.addCardCount(table._logic.get_card_count_by_index(table.GRR._cards_index[i]) - 1);
			} else {
				tableResponse.addCardCount(table._logic.get_card_count_by_index(table.GRR._cards_index[i]));
			}

		}

		// 数据
		tableResponse.setSendCardData(0);
		int hand_cards[] = new int[GameConstants.MAX_COUNT];
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[seat_index], hand_cards);

		// 如果断线重连的人是自己
		if (seat_index == _seat_index) {
			table._logic.remove_card_by_data(hand_cards, _send_card_data);
			hand_card_count--;
		}

		for (int i = 0; i < hand_card_count; i++) {
			tableResponse.addCardsData(hand_cards[i]);
		}

		roomResponse.setTable(tableResponse);
		table.send_response_to_player(seat_index, roomResponse);

		if (_seat_index == seat_index) {
			// 如果可以报听,刷新自己的手牌
			int ting_count = table._playerStatus[_seat_index]._hu_out_card_count;
			if (ting_count > 0) {

				// 刷新手牌
				// int cards[] = new int[MJGameConstants.MAX_COUNT];
				// 刷新自己手牌
				// hand_card_count =
				// table._logic.switch_to_cards_data(table.GRR._cards_index[_seat_index],
				// cards);

				for (int i = 0; i < hand_card_count; i++) {
					for (int j = 0; j < ting_count; j++) {
						if (hand_cards[i] == table._playerStatus[_seat_index]._hu_out_card_ting[j]) {
							hand_cards[i] += GameConstants.CARD_ESPECIAL_TYPE_TING;
						}
					}
				}

				table.operate_player_cards_with_ting(_seat_index, hand_card_count, hand_cards, 0, null);
			}
		} else {
			int ting_cards[] = table._playerStatus[seat_index]._hu_cards;
			int ting_count = table._playerStatus[seat_index]._hu_card_count;

			if (ting_count > 0) {
				table.operate_chi_hu_cards(seat_index, ting_count, ting_cards);
			}
		}

		int real_card = _send_card_data;

		if (ting_send_card) {
			real_card += GameConstants.CARD_ESPECIAL_TYPE_TING;
		} else if (table._logic.is_magic_card(_send_card_data)) {
			real_card += GameConstants.CARD_ESPECIAL_TYPE_HUN;
		}

		// 发送数据
		// 只有自己才有数值
		table.operate_player_get_card(_seat_index, 1, new int[] { real_card }, seat_index);
		
		if(table.GRR._left_card_count == 0){
			table.operate_out_card(_seat_index, 1, new int[]{_send_card_data},
	                GameConstants.OUT_CARD_TYPE_LEFT, GameConstants.INVALID_SEAT);
		}

		if (table._playerStatus[seat_index].has_action() && (table._playerStatus[seat_index].is_respone() == false)) {
			table.operate_player_action(seat_index, false);
		}

		return true;
	}
}
