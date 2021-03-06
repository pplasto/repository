package com.cai.game.ddz.handler.klddz;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.constant.game.mj.Constants_KL_DDZ;
import com.cai.common.domain.PlayerStatus;
import com.cai.common.util.PBUtil;
import com.cai.future.GameSchedule;
import com.cai.future.runnable.DDZAutoOpreateRunnable;
import com.cai.future.runnable.DDZAutoOutCardRunnable;
import com.cai.future.runnable.GameFinishRunnable;
import com.cai.game.ddz.DDZTable;
import com.cai.game.ddz.handler.DDZHandlerOutCardOperate;
import com.cai.service.PlayerServiceImpl;

import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.ddz.DdzRsp.Effect_Action;
import protobuf.clazz.ddz.DdzRsp.RoomInfoDdz;
import protobuf.clazz.ddz.DdzRsp.TableResponseDDZ;

public class DDZHandlerOutCardOperate_KL extends DDZHandlerOutCardOperate<DDZ_KL_Table> {
	@Override
	public void exe(DDZ_KL_Table table) {
		// TODO Auto-generated method stub
		PlayerStatus playerStatus = table._playerStatus[_out_card_player];
		playerStatus.reset();
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			table._playerStatus[i].clean_action();
			table._playerStatus[i].clean_status();
		}
		table._prev_palyer = _out_card_player;

		// 玩家不出
		if (this._b_out_card == 0) {
			if (table._auto_out_card_scheduled != null) {
				table._auto_out_card_scheduled.cancel(false);
			}
			if (table._trustee_auto_opreate_scheduled[_out_card_player] != null) {
				table._trustee_auto_opreate_scheduled[_out_card_player].cancel(false);
			}
			table._auto_out_card_scheduled = null;
			table._trustee_auto_opreate_scheduled[_out_card_player] = null;
			no_out_card(table);
			return;
		}

		int cbCardType = adjust_out_card_right(table);
		if (cbCardType == GameConstants.DDZ_CT_ERROR) {
			return;
		}
		if (table._auto_out_card_scheduled != null) {
			table._auto_out_card_scheduled.cancel(false);
		}
		if (table._trustee_auto_opreate_scheduled[_out_card_player] != null) {
			table._trustee_auto_opreate_scheduled[_out_card_player].cancel(false);
		}
		table._auto_out_card_scheduled = null;
		table._trustee_auto_opreate_scheduled[_out_card_player] = null;

		if (cbCardType == GameConstants.DDZ_CT_BOMB_CARD_JX || cbCardType == GameConstants.DDZ_CT_MISSILE_CARD_JX) {
			// if (table._logic.get_card_value(_out_change_cards_data[0]) == 3
			// && _out_card_count == 4) {
			// table._times *= 8;
			// for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			// table._user_times[i] += 3;
			// }
			// } else {
			table._times *= 2;
			table.jia_bei_operate();

			table._boom_count++;

			// 比赛场更新数据
			PlayerServiceImpl.getInstance().updateRoomInfo(table.getRoom_id());
		}
		table._out_card_times[_out_card_player]++;

		int pre_turn_out_type = table._turn_out_card_type;
		int pre_out_player = table._out_card_player;
		// 保存当前出牌玩家
		table._out_card_player = _out_card_player;
		table._turn_out_card_type = cbCardType;
		table._turn_out__player = _out_card_player;
		// 保存该轮出牌信息
		table.GRR._cur_round_pass[_out_card_player] = 0;
		table.GRR._cur_round_count[_out_card_player] = this._out_card_count;
		for (int i = 0; i < this._out_card_count; i++) {
			table.GRR._cur_round_data[_out_card_player][i] = this._out_change_cards_data[i];
			// 保存该次出牌数据
			table._turn_out_card_data[i] = this._out_change_cards_data[i];
			table._turn_out_real_data[i] = this._out_cards_data[i];
		}
		table._turn_out_card_count = this._out_card_count;
		table.GRR._card_count[_out_card_player] -= this._out_card_count;

		// 刷新手牌
		// int cards[] = new int[GameConstants.MAX_HH_COUNT];

		// 清空接下去出牌玩家出牌数据
		int next_player = (_out_card_player + table.getPlayerCount() + 1) % table.getPlayerCount();
		if (0 != table.GRR._card_count[_out_card_player]) {
			table._current_player = next_player;
			table.GRR._cur_round_count[table._current_player] = 0;
			table.GRR._cur_round_pass[table._current_player] = 0;
			for (int j = 0; j < this._out_card_count; j++) {
				table.GRR._cur_round_data[table._current_player][j] = GameConstants.INVALID_CARD;
			}
		} else {
			table._current_player = GameConstants.INVALID_SEAT;
		}

		// 显示出牌
		table.operate_out_card(table._out_card_player, table._turn_out_card_count, table._turn_out_card_data,
				cbCardType, GameConstants.INVALID_SEAT);

		// 玩家效果
		if (pre_turn_out_type > GameConstants.DDZ_CT_THREE
				&& (_out_card_player == table.GRR._banker_player || pre_out_player == table.GRR._banker_player)) {
			RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
			Effect_Action.Builder effect_action = Effect_Action.newBuilder();
			roomResponse.setType(MsgConstants.RESPONSE_DDZ_EFFECT);// 201
			for (int i = 0; i < table.getTablePlayerNumber(); i++) {
				if (i == _out_card_player) {
					effect_action.addSmileStatus(1);
				} else {
					effect_action.addSmileStatus(0);
				}
				if (i == pre_out_player) {
					effect_action.addCryStatus(1);
				} else {
					effect_action.addCryStatus(0);
				}
			}
			roomResponse.setCommResponse(PBUtil.toByteString(effect_action));
			table.send_response_to_room(roomResponse);
		}
		if (0 == table.GRR._card_count[_out_card_player]) {
			int delay = 1;
			table._banker_select = _out_card_player;
			GameSchedule.put(
					new GameFinishRunnable(table.getRoom_id(), _out_card_player, GameConstants.Game_End_NORMAL), delay,
					TimeUnit.SECONDS);
			return;
		}

		int delay = 0;
		if (!table._logic.SearchOutCard(table.GRR._cards_data[table._current_player],
				table.GRR._card_count[table._current_player], table._turn_out_card_data, table._turn_out_card_count,
				table._turn_out_card_type)) {
			delay = 3;
			table._auto_out_card_scheduled = GameSchedule.put(
					new DDZAutoOutCardRunnable(table.getRoom_id(), table._current_player, table), delay,
					TimeUnit.SECONDS);
		}

		if (table.istrustee[table._current_player]) {
			table._trustee_auto_opreate_scheduled[table._current_player] = GameSchedule.put(
					new DDZAutoOpreateRunnable(table.getRoom_id(), table, table._current_player), 1, TimeUnit.SECONDS);
		}

	}

	public int adjust_out_card_right(DDZTable table) {
		// table._logic.make_change_card(_out_change_cards_data,
		// _out_card_count);

		// table._logic.sort_card_date_list(_out_change_cards_data,
		// _out_card_count);
		boolean isLast = false;
		if (table.GRR._card_count[_out_card_player] == _out_card_count) {
			isLast = true;
		}
		int cbCardType = table._logic.GetCardType(this._out_change_cards_data, this._out_card_count,
				this._out_cards_data, isLast, _b_out_card);
		if (cbCardType == GameConstants.DDZ_CT_ERROR_JX) {
			table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
			return cbCardType;
		}
		if (!table._logic.make_hua_card(this._out_change_cards_data, this._out_card_count, this._out_cards_data)) {
			table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
			return GameConstants.DDZ_CT_ERROR_JX;
		}
		// if (cbCardType == GameConstants.DDZ_CT_THREE_JX || cbCardType ==
		// GameConstants.DDZ_CT_THREE_LINE_JX) {
		// if (table.GRR._card_count[_out_card_player] != _out_card_count) {
		// table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
		// return GameConstants.DDZ_CT_ERROR_JX;
		// }
		// }
		if (cbCardType != GameConstants.DDZ_CT_FOUR_TAKE_TWO_JX) {
			table._logic.sort_card_date_list_by_type(this._out_change_cards_data, this._out_card_count, cbCardType);
		}
		if (table._turn_out_card_count > 0) {
			if (!table._logic.CompareCard(table._turn_out_card_data, _out_change_cards_data, table._turn_out_card_count,
					_out_card_count, table._turn_out_card_type, _b_out_card)) {
				table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
				return GameConstants.DDZ_CT_ERROR_JX;
			}
		} else {
			// if (cbCardType == GameConstants.DDZ_CT_THREE_LINE_JX_ONE) {
			// if (_out_card_count != table.GRR._card_count[_out_card_player]) {
			// table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
			// return GameConstants.DDZ_CT_ERROR_JX;
			// }
			// }
		}
		if (table.GRR._card_count[_out_card_player] - _out_card_count == 1) {
			int hand_data[] = new int[table.GRR._card_count[_out_card_player]];
			for (int i = 0; i < table.GRR._card_count[_out_card_player]; i++) {
				hand_data[i] = table.GRR._cards_data[_out_card_player][i];
			}
			if (!table._logic.remove_cards_by_data(hand_data, table.GRR._card_count[_out_card_player],
					this._out_cards_data, this._out_card_count)) {
				table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
				return GameConstants.DDZ_CT_ERROR_JX;
			}
			if (hand_data[0] == 0x5E) {
				table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
				return GameConstants.DDZ_CT_ERROR_JX;
			}
		}
		if (!table._logic.remove_cards_by_data(table.GRR._cards_data[_out_card_player],
				table.GRR._card_count[_out_card_player], this._out_cards_data, this._out_card_count)) {
			table.send_error_notify(_out_card_player, 2, "请选择正确的牌型!");
			return GameConstants.DDZ_CT_ERROR_JX;
		}
		return cbCardType;
	}

	// 玩家不出
	public void no_out_card(DDZTable table) {

		table.GRR._cur_round_count[_out_card_player] = 0;
		table.GRR._cur_round_pass[_out_card_player] = 1;

		if (table._turn_out_card_count == 0) {
			return;
		}

		for (int i = 0; i < table.get_hand_card_count_max(); i++) {
			table.GRR._cur_round_data[_out_card_player][i] = GameConstants.INVALID_CARD;
		}

		// 判断下一个玩家
		int next_player = (_out_card_player + table.getPlayerCount() + 1) % table.getPlayerCount();
		table._current_player = next_player;
		table.GRR._cur_round_count[table._current_player] = 0;
		table.GRR._cur_round_pass[table._current_player] = 0;
		for (int j = 0; j < this._out_card_count; j++) {
			table.GRR._cur_round_data[table._current_player][j] = GameConstants.INVALID_CARD;
		}
		if (table._current_player == table._out_card_player) {
			// 出完一圈牌
			table._turn_out_card_count = 0;
			for (int i = 0; i < table._turn_out_card_count; i++) {
				table._turn_out_card_data[i] = GameConstants.INVALID_CARD;
			}
			table._turn_out_card_count = 0;
			table._turn_out_card_type = GameConstants.DDZ_CT_ERROR;
			Arrays.fill(table._turn_out_card_data, GameConstants.INVALID_CARD);
			Arrays.fill(table.GRR._cur_round_count, 0);
			Arrays.fill(table.GRR._cur_round_pass, 0);
		}

		// 显示出牌
		table.operate_out_card(_out_card_player, 0, _out_cards_data, GameConstants.DDZ_CT_PASS,
				GameConstants.INVALID_SEAT);
		if (table._current_player == table._out_card_player && table.GRR._card_count[table._current_player] == 1) {
			int delay = 0;
			if (table.istrustee[table._current_player]) {
				delay = 1;
			} else {
				delay = 2;
			}
			table._auto_out_card_scheduled = GameSchedule.put(
					new DDZAutoOutCardRunnable(table.getRoom_id(), table._current_player, table), delay,
					TimeUnit.SECONDS);
		} else {
			int delay = 0;
			if (!table._logic.SearchOutCard(table.GRR._cards_data[table._current_player],
					table.GRR._card_count[table._current_player], table._turn_out_card_data, table._turn_out_card_count,
					table._turn_out_card_type)) {
				delay = 3;
				table._auto_out_card_scheduled = GameSchedule.put(
						new DDZAutoOutCardRunnable(table.getRoom_id(), table._current_player, table), delay,
						TimeUnit.SECONDS);
			}

			if (table.istrustee[table._current_player]) {
				table._trustee_auto_opreate_scheduled[table._current_player] = GameSchedule.put(
						new DDZAutoOpreateRunnable(table.getRoom_id(), table, table._current_player), 1,
						TimeUnit.SECONDS);
			}
		}

	}

	@Override
	public boolean handler_player_be_in_room(DDZ_KL_Table table, int seat_index) {
		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_DDZ_RECONNECT_DATA);

		TableResponseDDZ.Builder tableResponse_ddz = TableResponseDDZ.newBuilder();
		table.load_player_info_data_reconnect(tableResponse_ddz);
		RoomInfoDdz.Builder room_info = table.getRoomInfoDdz();
		tableResponse_ddz.setRoomInfo(room_info);

		tableResponse_ddz.setBankerPlayer(table.GRR._banker_player);
		tableResponse_ddz.setCurrentPlayer(table._current_player);
		tableResponse_ddz.setPrevPlayer(table._prev_palyer);

		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			tableResponse_ddz.addOutCardsCount(table.GRR._cur_round_count[i]);
			tableResponse_ddz.addPlayerPass(table.GRR._cur_round_pass[i]);
			Int32ArrayResponse.Builder out_cards = Int32ArrayResponse.newBuilder();
			Int32ArrayResponse.Builder out_change_cards = Int32ArrayResponse.newBuilder();
			for (int j = 0; j < table.GRR._cur_round_count[i]; j++) {
				if (table.GRR._cur_round_count[i] > 0) {
					out_cards.addItem(table.GRR._cur_round_data[i][j]);
					out_change_cards.addItem(table.GRR._cur_round_data[i][j]);
				}
			}
			tableResponse_ddz.addCardCount(table.GRR._card_count[i]);
			tableResponse_ddz.addCardType(table.GRR._cur_card_type[i]);
			tableResponse_ddz.addOutCardsData(i, out_cards);
			tableResponse_ddz.addChangeCardsData(out_change_cards);
			Int32ArrayResponse.Builder cards_card = Int32ArrayResponse.newBuilder();
			tableResponse_ddz.addCardsData(i, cards_card);
			tableResponse_ddz.addQiangBanker(table._qiang_banker[i]);
			tableResponse_ddz.addCallBanker(table._call_banker[i]);
			tableResponse_ddz.addAddTimes(table._add_times[i]);
		}
		tableResponse_ddz.addQiangAction(table._qiang_action[0]);
		tableResponse_ddz.addQiangAction(table._qiang_action[1]);
		tableResponse_ddz.addCallAction(table._call_action[0]);
		tableResponse_ddz.addCallAction(table._call_action[1]);
		tableResponse_ddz.addCallAction(table._call_action[2]);
		tableResponse_ddz.addCallAction(table._call_action[3]);

		// if (seat_index == table.GRR._banker_player) {
		for (int i = 0; i < table._di_pai_card_count; i++) {
			tableResponse_ddz.addDiCardsData(table._di_pai_card_data[i]);
		}
		// }
		// else {
		// for (int i = 0; i < table._di_pai_card_count; i++) {
		// tableResponse_ddz.addDiCardsData(GameConstants.BLACK_CARD);
		// }
		// }

		tableResponse_ddz.setDiCardCount(table._di_pai_card_count);
		tableResponse_ddz.setDiCardsType(table._di_pai_type);

		// 手牌--将自己的手牌数据发给自己
		Int32ArrayResponse.Builder cards_card = Int32ArrayResponse.newBuilder();
		for (int j = 0; j < table.GRR._card_count[seat_index]; j++) {
			if (table._logic.switch_card_to_idnex(table.GRR._cards_data[seat_index][j]) == table._logic.magic_card[0]
					&& table._magic_card != GameConstants.INVALID_CARD) {
				cards_card.addItem(table.GRR._cards_data[seat_index][j] + 0x100);
			} else {
				cards_card.addItem(table.GRR._cards_data[seat_index][j]);
			}

		}
		tableResponse_ddz.setCardsData(seat_index, cards_card);
		for (int i = 0; i < table._turn_out_card_count; i++) {
			if (table._turn_out_card_count > 0) {
				tableResponse_ddz.addPrCardsData(table._turn_out_card_data[i]);
				tableResponse_ddz.addPrChangeCardsData(table._turn_out_card_data[i]);
			}
		}
		tableResponse_ddz.setPrCardsCount(table._turn_out_card_count);
		tableResponse_ddz.setPrOutCardType(table._turn_out_card_type);
		tableResponse_ddz.setPrOutCardPlayer(table._turn_out__player);
		if (table._turn_out_card_count == 0) {
			tableResponse_ddz.setIsFirstOut(1);
		} else {
			tableResponse_ddz.setIsFirstOut(0);
		}
		if (table._current_player != GameConstants.INVALID_SEAT) {
			if (table._turn_out_card_count == 0) {
				tableResponse_ddz.setDisplayTime(20);
				if (table._current_player == seat_index) {
					tableResponse_ddz.setCurPlayerYaPai(1);
				}
			} else {
				boolean bCanBig = table._logic.SearchOutCard(table.GRR._cards_data[table._current_player],
						table.GRR._card_count[table._current_player], table._turn_out_card_data,
						table._turn_out_card_count, table._turn_out_card_type);
				if (!bCanBig) {
					tableResponse_ddz.setDisplayTime(3);

				} else {
					tableResponse_ddz.setDisplayTime(20);
				}
				if (table._current_player == seat_index) {
					if (!bCanBig) {
						tableResponse_ddz.setCurPlayerYaPai(0);
					} else {
						tableResponse_ddz.setCurPlayerYaPai(1);
					}
				} else {
					tableResponse_ddz.setDisplayTime(20);
					tableResponse_ddz.setCurPlayerYaPai(1);
				}
			}
		} else {
			tableResponse_ddz.setDisplayTime(20);
			tableResponse_ddz.setCurPlayerYaPai(1);
		}

		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			tableResponse_ddz.addDifenBombDes(table.get_boom_difen_des(i));
		}
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			tableResponse_ddz.addPiao(table._piao_fen[i]);
			tableResponse_ddz.addIsPiao(table._player_result.pao[i]);
		}
		roomResponse.setCommResponse(PBUtil.toByteString(tableResponse_ddz));

		table.send_response_to_player(seat_index, roomResponse);

		if (table.ming_pai) {
			table.ming_pai_operate();
		}
		if (table.has_rule(Constants_KL_DDZ.GAME_RULE_SUI_JI_LAI_ZI)) {
			table.send_lai_zi(0, seat_index);
		}
		return true;
	}
}
