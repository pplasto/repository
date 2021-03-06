package com.cai.game.hh.handler;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.domain.GangCardResult;
import com.cai.game.hh.HHTable;

import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.TableResponse;
import protobuf.clazz.Protocol.WeaveItemResponse;
import protobuf.clazz.Protocol.WeaveItemResponseArrayResponse;

public class HHHandlerChiPeng<T extends HHTable> extends HHHandler<T> {
	protected int _seat_index = GameConstants.INVALID_SEAT;
	protected int _action;
	protected int _card;
	protected int _provider;
	protected int _type;
	protected int _lou_card;

	protected GangCardResult m_gangCardResult;

	public HHHandlerChiPeng() {
		_type = 0;
		m_gangCardResult = new GangCardResult();
	}

	public void reset_status(int seat_index, int provider, int action, int card, int type,int lou_card) {
		_seat_index = seat_index;
		_action = action;
		_card = card;
		_provider = provider;
		_type = type;
		_lou_card = lou_card;
	}

	// 执行吃碰操作
	@Override
	public void exe(T table) {
		// 组合扑克
		int wIndex = table.GRR._weave_count[_seat_index]++;
		table.GRR._weave_items[_seat_index][wIndex].public_card = 1;
		table.GRR._weave_items[_seat_index][wIndex].center_card = _card;
		table.GRR._weave_items[_seat_index][wIndex].weave_kind = _action;
		table.GRR._weave_items[_seat_index][wIndex].provide_player = _provider;

		// 设置用户
		table._current_player = _seat_index;

		// 效果
		table.operate_effect_action(_seat_index, GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[] { _action }, 1,
				GameConstants.INVALID_SEAT);

		// 删掉出来的那张牌
		// table.operate_out_card(this._provider, 0,
		// null,MJGameConstants.OUT_CARD_TYPE_MID,MJGameConstants.INVALID_SEAT);
		table.operate_remove_discard(this._provider, table.GRR._discard_count[_provider]);

		// 刷新手牌包括组合
		int cards[] = new int[GameConstants.MAX_HH_COUNT];
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[_seat_index], cards);
		table.operate_player_cards(_seat_index, hand_card_count, cards, table.GRR._weave_count[_seat_index],
				table.GRR._weave_items[_seat_index]);

		// //回放
		// PlayerStatus curPlayerStatus = table._playerStatus[_seat_index];
		// curPlayerStatus.reset();
		//
		// table._playerStatus[_seat_index].chi_hu_round_valid();//可以胡了
		//
		// m_gangCardResult.cbCardCount = 0;
		// // 如果牌堆还有牌，判断能不能杠
		// if (table.GRR._left_card_count > 0) {
		// //只检测手上的牌能不能杠
		// int cbActionMask =
		// table._logic.analyse_gang_card_all(table.GRR._cards_index[_seat_index],
		// table.GRR._weave_items[_seat_index],
		// table.GRR._weave_count[_seat_index], m_gangCardResult,false);
		//
		// if(cbActionMask!=0){
		// curPlayerStatus.add_action(MJGameConstants.WIK_GANG);//转转就是杠
		// for(int i= 0; i < m_gangCardResult.cbCardCount; i++){
		// //加上刚
		// curPlayerStatus.add_gang(m_gangCardResult.cbCardData[i], _seat_index,
		// m_gangCardResult.isPublic[i]);
		// }
		// }
		// }
		//
		// if (curPlayerStatus.has_action()) {
		// curPlayerStatus.set_status(MJGameConstants.Player_Status_OPR_CARD);//
		// 操作状态
		// table.operate_player_action(_seat_index,false);
		//
		// } else {
		// curPlayerStatus.set_status(MJGameConstants.Player_Status_OUT_CARD);//
		// 出牌状态
		// table.operate_player_status();
		// }

	}

	/***
	 * //用户出牌--吃碰之后的出牌
	 */
	@Override
	public boolean handler_player_out_card(T table, int seat_index, int card) {
		// 错误断言
		card = table.get_real_card(card);

		// 错误断言
		if (table._logic.is_valid_card(card) == false) {
			table.log_error("出牌,牌型出错");
			return false;
		}

		// 效验参数
		if (seat_index != _seat_index) {
			table.log_error("出牌,没到出牌");
			return false;
		}
		if(table._playerStatus[_seat_index].get_status() != GameConstants.Player_Status_OUT_CARD)
		{
			table.log_error("状态不对不能出牌");
			return false;
		}
		if(table.GRR._cards_index[_seat_index][table._logic.switch_to_card_index(card)] >= 3)
		{
			// 刷新手牌
			int cards[] = new int[GameConstants.MAX_HH_COUNT];
			// 显示出牌
			table.operate_out_card(_seat_index, 0, null, GameConstants.OUT_CARD_TYPE_MID,
					GameConstants.INVALID_SEAT);
			// 刷新自己手牌
			int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[_seat_index], cards);
			table.operate_player_cards(_seat_index, hand_card_count, cards, table.GRR._weave_count[_seat_index],table.GRR._weave_items[_seat_index]);

			table.log_error(_seat_index+"出牌出错 HHHandlerDispatchCard "+_seat_index);
			return true;
		}
		// 删除扑克
		if (table._logic.remove_card_by_index(table.GRR._cards_index[_seat_index], card) == false) {
			table.log_error("出牌删除出错");
			return false;
		}

		// 出牌--切换到出牌handler
		table.exe_out_card(_seat_index, card, _action);

		return true;
	}

	@Override
	public boolean handler_player_be_in_room(T table, int seat_index) {
		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_RECONNECT_DATA);

		TableResponse.Builder tableResponse = TableResponse.newBuilder();

		table.load_room_info_data(roomResponse);// 加载房间的玩法 状态信息
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
			tableResponse.addTrustee(table.istrustee[i]);// 是否托管
			// 剩余牌数
			tableResponse.addDiscardCount(table.GRR._discard_count[i]);
			Int32ArrayResponse.Builder int_array = Int32ArrayResponse.newBuilder();
			for (int j = 0; j < 55; j++) {
				int_array.addItem(table.GRR._discard_cards[i][j]);
			}
			tableResponse.addDiscardCards(int_array);

			// 组合扑克
			tableResponse.addWeaveCount(table.GRR._weave_count[i]);
			WeaveItemResponseArrayResponse.Builder weaveItem_array = WeaveItemResponseArrayResponse.newBuilder();
			for (int j = 0; j < GameConstants.MAX_WEAVE_HH; j++) {
				WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
				weaveItem_item.setProvidePlayer(table.GRR._weave_items[i][j].provide_player);
				weaveItem_item.setPublicCard(table.GRR._weave_items[i][j].public_card);
				weaveItem_item.setWeaveKind(table.GRR._weave_items[i][j].weave_kind);
				weaveItem_item.setHuXi(table.GRR._weave_items[i][j].hu_xi);
				if(seat_index!=i) {
					if((table.GRR._weave_items[i][j].weave_kind==GameConstants.WIK_TI_LONG || table.GRR._weave_items[i][j].weave_kind==GameConstants.WIK_AN_LONG
							|| table.GRR._weave_items[i][j].weave_kind==GameConstants.WIK_AN_LONG_LIANG) &&table.GRR._weave_items[i][j].public_card==0) {
						weaveItem_item.setCenterCard(0);
					}else {
						if(table.is_mj_type(GameConstants.GAME_TYPE_PHZ_XT)&&table.has_rule(GameConstants.GAME_RULE_DI_AN_WEI)
								&&table._xt_display_an_long[i] ==true)
							weaveItem_item.setCenterCard(0);
						else
							weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);
					}
				}else {
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);
				}
				weaveItem_array.addWeaveItem(weaveItem_item);
			}
			tableResponse.addWeaveItemArray(weaveItem_array);
			tableResponse.addHuXi(table._hu_xi[i]);
			//
			tableResponse.addWinnerOrder(0);

			// 牌
			tableResponse.addCardCount(table._logic.get_card_count_by_index(table.GRR._cards_index[i]));
		}

		// 数据
		tableResponse.setSendCardData(0);
		int hand_cards[] = new int[GameConstants.MAX_HH_COUNT];
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[seat_index], hand_cards);

		for (int i = 0; i < GameConstants.MAX_HH_COUNT; i++) {
			tableResponse.addCardsData(hand_cards[i]);
		}

		roomResponse.setTable(tableResponse);

		table.send_response_to_player(seat_index, roomResponse);
		if(table.is_mj_type(GameConstants.GAME_TYPE_THK_HY))
		{
			table.operate_cannot_card(seat_index,false);
			if(table._xian_ming_zhao[seat_index] == true)
				table.operate_must_out_card(seat_index, false);
		}
		if(table.is_mj_type(GameConstants.GAME_TYPE_PHZ_XT))
		{
			table.operate_cannot_card(seat_index,false);
			
		}
		// 效果
		table.operate_effect_action(_seat_index, GameConstants.EFFECT_ACTION_TYPE_ACTION, 1, new long[] { _action }, 1,
				seat_index);

		if (table._playerStatus[seat_index].has_action() && (table._playerStatus[seat_index].is_respone() == false)) {
			table.operate_player_action(seat_index, false);
		}
		if(table._is_xiang_gong[seat_index] == true)
			table.operate_player_xiang_gong_flag(seat_index,table._is_xiang_gong[seat_index]);
		return true;
	}

}
