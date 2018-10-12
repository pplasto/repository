package com.cai.mj.handler.xthh;

import java.util.concurrent.TimeUnit;

import com.cai.common.constant.MJGameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.future.GameSchedule;
import com.cai.future.runnable.FinishLaiGenRunnable;
import com.cai.mj.MJTable;
import com.cai.mj.handler.MJHandler;

import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.TableResponse;
import protobuf.clazz.Protocol.WeaveItemResponse;
import protobuf.clazz.Protocol.WeaveItemResponseArrayResponse;

/***
 * 赖根
 * @author Administrator
 *
 */
public class MJHandlerLaiGen extends MJHandler {
	protected int _lai_gen_card;
	
	protected int _banker;
	
	
	public void reset_status(int banker){
		_banker = banker;
	}
	@Override
	public void exe(MJTable table) {

		// 从牌堆拿出一张牌
		table._send_card_count++;
		if (table.is_mj_type(MJGameConstants.GAME_TYPE_HZ)) {
			_lai_gen_card = table._repertory_card_zz[table._all_card_len-table.GRR._left_card_count];
		} else {
			_lai_gen_card = table._repertory_card_cs[table._all_card_len-table.GRR._left_card_count];
		}
		--table.GRR._left_card_count;
		
		if(table.DEBUG_CARDS_MODE)_lai_gen_card=0x02;//
		//显示
		table.operate_show_card(_banker,MJGameConstants.Show_Card_Center, 1,new int[]{_lai_gen_card+MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN},MJGameConstants.INVALID_SEAT);
	
		int card_next=0;
		int cur_data = table._logic.get_card_value(_lai_gen_card);
		if(cur_data == 9){
			card_next = _lai_gen_card-8;
		}else{
			card_next = _lai_gen_card+1;
		}
		//添加赖根
		table._logic.add_lai_gen_card(_lai_gen_card);
		//添加癞子
		table._logic.add_magic_card_index(table._logic.switch_to_card_index(card_next));
		
		table.GRR._especial_card_count=1;
		table.GRR._especial_show_cards[0] = _lai_gen_card;
		
		table.GRR._especial_card_count=2;
		table.GRR._especial_show_cards[1] = card_next;
		table.GRR._especial_show_cards[1] +=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
		
		//延迟调度结束赖根
		GameSchedule.put(new FinishLaiGenRunnable(table.getRoom_id(), _banker),
				3, TimeUnit.SECONDS);
	}
	
	
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
		tableResponse.setCurrentPlayer(table.GRR._banker_player);
		tableResponse.setCellScore(0);

		// 状态变量
		tableResponse.setActionCard(0);
		//tableResponse.setActionMask((_response[seat_index] == false) ? _player_action[seat_index] : MJGameConstants.WIK_NULL);

		// 历史记录
		tableResponse.setOutCardData(0);
		tableResponse.setOutCardPlayer(0);

		for (int i = 0; i < MJGameConstants.GAME_PLAYER; i++) {
			tableResponse.addTrustee(false);// 是否托管
			// 剩余牌数
			tableResponse.addDiscardCount(table.GRR._discard_count[i]);
			Int32ArrayResponse.Builder int_array = Int32ArrayResponse.newBuilder();
			for (int j = 0; j < 55; j++) {
				if(table._logic.is_magic_card(table.GRR._discard_cards[i][j])){
					//癞子
					int_array.addItem(table.GRR._discard_cards[i][j]+MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI);
				}else{
					int_array.addItem(table.GRR._discard_cards[i][j]);
				}
				
			}
			tableResponse.addDiscardCards(int_array);

			// 组合扑克
			tableResponse.addWeaveCount(table.GRR._weave_count[i]);
			WeaveItemResponseArrayResponse.Builder weaveItem_array = WeaveItemResponseArrayResponse.newBuilder();
			for (int j = 0; j < MJGameConstants.MAX_WEAVE; j++) {
				WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
				if(table._logic.is_magic_card(table.GRR._weave_items[i][j].center_card)){
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card+MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI);
				}else if(table._logic.is_lai_gen_card(table.GRR._weave_items[i][j].center_card)){
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);//weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card+MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN);
				}else{
					weaveItem_item.setCenterCard(table.GRR._weave_items[i][j].center_card);
				}
				weaveItem_item.setProvidePlayer(table.GRR._weave_items[i][j].provide_player+MJGameConstants.WEAVE_SHOW_DIRECT);
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
		int cards[] = new int[MJGameConstants.MAX_COUNT];
		int hand_card_count = table._logic.switch_to_cards_data(table.GRR._cards_index[seat_index], cards);
		for(int j=0; j < hand_card_count; j++){
			if( table._logic.is_magic_card(cards[j])){
				cards[j]+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_ZI;
			}else if(table._logic.is_lai_gen_card(cards[j])){
				//cards[j]+=MJGameConstants.CARD_ESPECIAL_TYPE_LAI_GEN;
			}
		}

		for (int i = 0; i < MJGameConstants.MAX_COUNT; i++) {
			tableResponse.addCardsData(cards[i]);
		}

		roomResponse.setTable(tableResponse);

		table.send_response_to_player(seat_index, roomResponse);
		

		//显示
		table.operate_show_card(_banker,MJGameConstants.Show_Card_Center, 1,new int[_lai_gen_card],seat_index);
				
		return true;
	}

}