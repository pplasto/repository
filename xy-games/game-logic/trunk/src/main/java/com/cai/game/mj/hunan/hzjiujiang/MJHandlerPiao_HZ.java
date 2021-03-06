package com.cai.game.mj.hunan.hzjiujiang;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.constant.game.mj.GameConstants_HZJJ;
import com.cai.game.mj.MJTable;
import com.cai.game.mj.handler.AbstractMJHandler;

import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.TableResponse;

public class MJHandlerPiao_HZ extends AbstractMJHandler<MJTable_HZJJ> {
	@Override
	public void exe(MJTable_HZJJ table) {
		table._game_status = GameConstants.GS_MJ_PIAO;

		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_PAO_QIANG_ACTION);
		table.load_room_info_data(roomResponse);
		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			table._player_result.pao[i] = -1;
		}

		int paoMax = 0;
		if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO1)){
			paoMax = 1;
		}else if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO2)){
			paoMax = 2;
		}else if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO3)){
			paoMax = 3;
		}
		table.operate_player_data();

		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			if (table._player_result.pao[i] >= 5) {
				handler_pao_qiang(table, i, 5, 0);
				continue;
			}
			roomResponse.setTarget(i);
			roomResponse.setPao(table._player_result.pao[i]);
			roomResponse.setPaoMin(table._player_result.pao[i]);
			roomResponse.setPaoMax(paoMax);
			roomResponse.setPaoDes("最多飘3个");
			table.send_response_to_player(i, roomResponse);
		}
	}

	public boolean handler_pao_qiang(MJTable_HZJJ table, int seat_index, int pao, int qiang) {
		if (table._playerStatus[seat_index]._is_pao_qiang)
			return false;

		table._playerStatus[seat_index]._is_pao_qiang = true;

		table._player_result.pao[seat_index] = pao;

		table.operate_player_data();

		for (int i = 0; i < table.getTablePlayerNumber(); i++) {
			if (table._playerStatus[i]._is_pao_qiang == false) {
				return true;
			}
		}

		if (table._game_status == GameConstants.GS_MJ_PIAO) {
			table._game_status = GameConstants.GS_MJ_PLAY;
			table.on_game_start_hz_real();
		}

		return true;
	}

	@Override
	public boolean handler_player_be_in_room(MJTable_HZJJ table, int seat_index) {

		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_RECONNECT_DATA);

		TableResponse.Builder tableResponse = TableResponse.newBuilder();

		table.load_room_info_data(roomResponse);
		table.load_player_info_data(roomResponse);
		table.load_common_status(roomResponse);

		if (table._shang_zhuang_player != GameConstants.INVALID_SEAT) {
			tableResponse.setBankerPlayer(table._shang_zhuang_player);
		} else if (table._lian_zhuang_player != GameConstants.INVALID_SEAT) {
			tableResponse.setBankerPlayer(table._lian_zhuang_player);
		} else {
			tableResponse.setBankerPlayer(GameConstants.INVALID_SEAT);
		}

		roomResponse.setTable(tableResponse);

		table.send_response_to_player(seat_index, roomResponse);

		player_reconnect(table, seat_index);
		
		return true;
	}

	private void player_reconnect(MJTable_HZJJ table, int seat_index) {
		if (table._playerStatus[seat_index]._is_pao_qiang == true) {
			return;
		}

		RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
		roomResponse.setType(MsgConstants.RESPONSE_PAO_QIANG_ACTION);
		table.load_room_info_data(roomResponse);
		int paoMax = 0;
		if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO1)){
			paoMax = 1;
		}else if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO2)){
			paoMax = 2;
		}else if(table.has_rule(GameConstants_HZJJ.GAME_RULE_PIAO3)){
			paoMax = 3;
		}
		roomResponse.setTarget(seat_index);
		roomResponse.setPao(table._player_result.pao[seat_index]);
		roomResponse.setPaoMin(table._player_result.pao[seat_index]);
		roomResponse.setPaoMax(paoMax);
		roomResponse.setPaoDes("做多飘5个");
		table.send_response_to_player(seat_index, roomResponse);

		table.load_common_status(roomResponse);
		table.send_response_to_player(seat_index, roomResponse);
	}
}
