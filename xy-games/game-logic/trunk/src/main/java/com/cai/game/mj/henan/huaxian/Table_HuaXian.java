package com.cai.game.mj.henan.huaxian;

import com.cai.common.constant.GameConstants;
import com.cai.common.constant.MsgConstants;
import com.cai.common.constant.game.Constants_HuaXian;
import com.cai.common.domain.ChiHuRight;
import com.cai.common.domain.Player;
import com.cai.common.domain.PlayerStatus;
import com.cai.common.domain.WeaveItem;
import com.cai.game.mj.AbstractMJTable;
import com.cai.game.mj.FengKanUtil;
import com.cai.game.mj.MJType;
import com.cai.game.util.AnalyseCardUtil;
import com.cai.game.util.GameUtilConstants;
import com.cai.service.PlayerServiceImpl;

import protobuf.clazz.Protocol.GameEndResponse;
import protobuf.clazz.Protocol.GameStartResponse;
import protobuf.clazz.Protocol.Int32ArrayResponse;
import protobuf.clazz.Protocol.RoomInfo;
import protobuf.clazz.Protocol.RoomResponse;
import protobuf.clazz.Protocol.WeaveItemResponse;
import protobuf.clazz.Protocol.WeaveItemResponseArrayResponse;

public class Table_HuaXian extends AbstractMJTable {
    private static final long serialVersionUID = 2570006934151976528L;

    private int feng_kan = 0;
    protected HandlerPao_HuaXian _handler_pao;

    public Table_HuaXian() {
        super(MJType.GAME_TYPE_HE_NAN_HUA_XIAN);
    }

    @Override
    public int analyse_chi_hu_card(int[] cards_index, WeaveItem[] weaveItems, int weave_count, int cur_card,
            ChiHuRight chiHuRight, int card_type, int _seat_index) {
        if (!_logic.is_valid_card(cur_card)) {
            return GameConstants.WIK_NULL;
        }

        int cbChiHuKind = GameConstants.WIK_NULL;

        int[] magic_cards_index = new int[GameUtilConstants.MAX_MAGIC_INDEX_COUNT];
        int magic_card_count = 0;

        boolean can_win = false;
        boolean has_dong_feng_ling = has_rule(Constants_HuaXian.GAME_RULE_DONG_FENG_LING);

        if (has_dong_feng_ling) {
            can_win = AnalyseCardUtil.analyse_feng_chi_dfl_by_cards_index(cards_index,
                    _logic.switch_to_card_index(cur_card), magic_cards_index, magic_card_count);
        } else {
            can_win = AnalyseCardUtil.analyse_feng_chi_by_cards_index(cards_index,
                    _logic.switch_to_card_index(cur_card), magic_cards_index, magic_card_count);
        }

        if (can_win == false) {
            chiHuRight.set_empty();
            return cbChiHuKind;
        }

        cbChiHuKind = GameConstants.WIK_CHI_HU;

        if (card_type == Constants_HuaXian.HU_CARD_TYPE_ZI_MO) {
            chiHuRight.opr_or(Constants_HuaXian.CHR_ZI_MO);
        } else if (card_type == Constants_HuaXian.HU_CARD_TYPE_JIE_PAO) {
            chiHuRight.opr_or(Constants_HuaXian.CHR_JIE_PAO);
        }

        int[] tmp_cards_index = new int[GameConstants.MAX_INDEX];
        for (int i = 0; i < GameConstants.MAX_INDEX; i++) {
            tmp_cards_index[i] = cards_index[i];
        }
        tmp_cards_index[_logic.switch_to_card_index(cur_card)]++;

        int[] tmp_feng_kan = new int[2];
        FengKanUtil.getFengKanCount(tmp_cards_index, tmp_feng_kan, has_dong_feng_ling);

        feng_kan = tmp_feng_kan[0] + tmp_feng_kan[1];

        int colorCount = _logic.get_se_count(tmp_cards_index, weaveItems, weave_count);
        chiHuRight.duanmen_count = 3 - colorCount;

        return cbChiHuKind;
    }

    @Override
    public boolean estimate_gang_respond(int seat_index, int card) {
        boolean bAroseAction = false;

        PlayerStatus playerStatus = null;

        int action = GameConstants.WIK_NULL;

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            if (seat_index == i)
                continue;

            playerStatus = _playerStatus[i];

            if (playerStatus.is_chi_hu_round()) {
                ChiHuRight chr = GRR._chi_hu_rights[i];
                chr.set_empty();

                action = analyse_chi_hu_card(GRR._cards_index[i], GRR._weave_items[i], GRR._weave_count[i], card, chr,
                        Constants_HuaXian.HU_CARD_TYPE_QIANG_GANG, i);

                if (action != 0) {
                    _playerStatus[i].add_action(GameConstants.WIK_CHI_HU);
                    _playerStatus[i].add_chi_hu(card, seat_index);
                    bAroseAction = true;
                }
            }
        }

        if (bAroseAction == true) {
            _provide_player = seat_index;
            _provide_card = card;
            _resume_player = _current_player;
            _current_player = GameConstants.INVALID_SEAT;
        }

        return bAroseAction;
    }

    public boolean estimate_player_out_card_respond(int seat_index, int card, int type) {
        boolean bAroseAction = false;

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            _playerStatus[i].clean_action();
            _playerStatus[i].clean_weave();
        }

        PlayerStatus playerStatus = null;

        int action = GameConstants.WIK_NULL;

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            if (seat_index == i) {
                continue;
            }

            playerStatus = _playerStatus[i];

            if (GRR._left_card_count > 0) {
                action = _logic.check_peng(GRR._cards_index[i], card);
                if (action != 0) {
                    playerStatus.add_action(action);
                    playerStatus.add_peng(card, seat_index);
                    bAroseAction = true;
                }

                if (GRR._left_card_count > 0) {
                    action = _logic.estimate_gang_card_out_card(GRR._cards_index[i], card);
                    if (action != 0) {
                        playerStatus.add_action(GameConstants.WIK_GANG);
                        playerStatus.add_gang(card, seat_index, 1);
                        bAroseAction = true;
                    }
                }
            }

            if (_playerStatus[i].is_chi_hu_round()) {
                if (has_rule(Constants_HuaXian.GAME_RULE_DONG_FENG_LING))
                    if (card == 0x31)
                        continue;

                ChiHuRight chr = GRR._chi_hu_rights[i];
                chr.set_empty();
                int cbWeaveCount = GRR._weave_count[i];

                int card_type = Constants_HuaXian.HU_CARD_TYPE_JIE_PAO;

                action = analyse_chi_hu_card(GRR._cards_index[i], GRR._weave_items[i], cbWeaveCount, card, chr,
                        card_type, i);

                if (action != 0) {
                    _playerStatus[i].add_action(GameConstants.WIK_CHI_HU);
                    _playerStatus[i].add_chi_hu(card, seat_index);
                    bAroseAction = true;
                }
            }
        }

        if (bAroseAction) {
            _resume_player = _current_player;
            _current_player = GameConstants.INVALID_SEAT;
            _provide_player = seat_index;
        }

        return bAroseAction;
    }

    public int get_ting_card(int[] cards, int cards_index[], WeaveItem weaveItem[], int cbWeaveCount, int seat_index) {
        int cbCardIndexTemp[] = new int[GameConstants.MAX_INDEX];
        for (int i = 0; i < GameConstants.MAX_INDEX; i++) {
            cbCardIndexTemp[i] = cards_index[i];
        }

        ChiHuRight chr = new ChiHuRight();

        int count = 0;
        int cbCurrentCard;

        int max_ting_count = GameConstants.MAX_ZI_FENG;

        for (int i = 0; i < max_ting_count; i++) {
            cbCurrentCard = _logic.switch_to_card_data(i);
            chr.set_empty();

            if (GameConstants.WIK_CHI_HU == analyse_chi_hu_card(cbCardIndexTemp, weaveItem, cbWeaveCount, cbCurrentCard,
                    chr, Constants_HuaXian.HU_CARD_TYPE_ZI_MO, seat_index)) {
                cards[count] = cbCurrentCard;
                count++;
            }
        }

        if (count == 0) {
        } else if (count == max_ting_count) {
            count = 1;
            cards[0] = -1;
        }

        return count;
    }

    @Override
    protected void onInitTable() {
        _handler_chi_peng = new HandlerChiPeng_HuaXian();
        _handler_dispath_card = new HandlerDispatchCard_HuaXian();
        _handler_gang = new HandlerGang_HuaXian();
        _handler_out_card_operate = new HandlerOutCardOperate_HuaXian();
        _handler_pao = new HandlerPao_HuaXian();
    }

    @Override
    protected boolean on_game_start() {
        if (has_rule(Constants_HuaXian.GAME_RULE_XIA_PAO)) {
            set_handler(_handler_pao);
            _handler_pao.exe(this);
        } else {
            on_game_start_real();
        }

        return true;
    }

    protected boolean on_game_start_real() {
        feng_kan = 0;

        _game_status = GameConstants.GS_MJ_PLAY;

        GameStartResponse.Builder gameStartResponse = GameStartResponse.newBuilder();
        gameStartResponse.setBankerPlayer(GRR._banker_player);
        gameStartResponse.setCurrentPlayer(_current_player);
        gameStartResponse.setLeftCardCount(GRR._left_card_count);

        int hand_cards[][] = new int[getTablePlayerNumber()][GameConstants.MAX_COUNT];

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            int hand_card_count = _logic.switch_to_cards_data(GRR._cards_index[i], hand_cards[i]);
            gameStartResponse.addCardsCount(hand_card_count);
        }

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            Int32ArrayResponse.Builder cards = Int32ArrayResponse.newBuilder();

            gameStartResponse.clearCardData();
            for (int j = 0; j < GameConstants.MAX_COUNT; j++) {
                gameStartResponse.addCardData(hand_cards[i][j]);
            }

            GRR._video_recode.addHandCards(cards);

            RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
            load_room_info_data(roomResponse);
            load_common_status(roomResponse);
            roomResponse.setType(MsgConstants.RESPONSE_GAME_START);
            roomResponse.setGameStart(gameStartResponse);
            roomResponse
                    .setCurrentPlayer(_current_player == GameConstants.INVALID_SEAT ? _resume_player : _current_player);
            roomResponse.setLeftCardCount(GRR._left_card_count);
            roomResponse.setGameStatus(_game_status);
            roomResponse.setLeftCardCount(GRR._left_card_count);
            send_response_to_player(i, roomResponse);
        }

        RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
        roomResponse.setType(MsgConstants.RESPONSE_GAME_START);
        load_room_info_data(roomResponse);
        load_common_status(roomResponse);
        for (int i = 0; i < getTablePlayerNumber(); i++) {
            Int32ArrayResponse.Builder cards = Int32ArrayResponse.newBuilder();

            for (int j = 0; j < GameConstants.MAX_COUNT; j++) {
                cards.addItem(hand_cards[i][j]);
            }
            gameStartResponse.addCardsData(cards);
        }
        roomResponse.setGameStart(gameStartResponse);
        roomResponse.setLeftCardCount(GRR._left_card_count);
        GRR.add_room_response(roomResponse);

        // 检测听牌
        for (int i = 0; i < getTablePlayerNumber(); i++) {
            _playerStatus[i]._hu_card_count = get_ting_card(_playerStatus[i]._hu_cards, GRR._cards_index[i],
                    GRR._weave_items[i], GRR._weave_count[i], i);
            if (_playerStatus[i]._hu_card_count > 0) {
                operate_chi_hu_cards(i, _playerStatus[i]._hu_card_count, _playerStatus[i]._hu_cards);
            }
        }

        exe_dispatch_card(_current_player, GameConstants.WIK_NULL, GameConstants.DELAY_SEND_CARD_DELAY);

        return true;
    }

    @Override
    protected boolean on_handler_game_finish(int seat_index, int reason) {
        int real_reason = reason;

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            _player_ready[i] = 0;
        }

        RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
        roomResponse.setType(MsgConstants.RESPONSE_GAME_END);
        GameEndResponse.Builder game_end = GameEndResponse.newBuilder();

        roomResponse.setLeftCardCount(0);

        load_common_status(roomResponse);
        load_room_info_data(roomResponse);

        RoomInfo.Builder room_info = getRoomInfo();
        game_end.setRoomInfo(room_info);
        game_end.setRunPlayerId(_run_player_id);
        game_end.setRoundOverType(0);
        game_end.setGamePlayerNumber(getTablePlayerNumber());
        game_end.setEndTime(System.currentTimeMillis() / 1000L);
        
        setGameEndBasicPrama(game_end);
        
        if (GRR != null) {
            game_end.setRoundOverType(1);
            game_end.setStartTime(GRR._start_time);

            game_end.setGameTypeIndex(GRR._game_type_index);
            roomResponse.setLeftCardCount(GRR._left_card_count);

            for (int i = 0; i < GRR._especial_card_count; i++) {
                game_end.addEspecialShowCards(GRR._especial_show_cards[i]);
            }

            GRR._end_type = reason;

            float lGangScore[] = new float[getTablePlayerNumber()];

            for (int i = 0; i < getTablePlayerNumber(); i++) {
                // TODO: 荒庄荒杠
                if (GRR._end_type != GameConstants.Game_End_DRAW) {
                    for (int j = 0; j < GRR._gang_score[i].gang_count; j++) {
                        for (int k = 0; k < getTablePlayerNumber(); k++) {
                            lGangScore[k] += GRR._gang_score[i].scores[j][k];
                        }
                    }
                }

                for (int j = 0; j < getTablePlayerNumber(); j++) {
                    _player_result.lost_fan_shu[i][j] += GRR._lost_fan_shu[i][j];
                }
            }

            for (int i = 0; i < getTablePlayerNumber(); i++) {
                GRR._game_score[i] += lGangScore[i];
                GRR._game_score[i] += GRR._start_hu_score[i];

                _player_result.game_score[i] += GRR._game_score[i];
            }

            load_player_info_data(roomResponse);

            game_end.setGameRound(_game_round);
            game_end.setCurRound(_cur_round);

            game_end.setCellScore(GameConstants.CELL_SCORE);

            game_end.setBankerPlayer(GRR._banker_player);
            game_end.setLeftCardCount(GRR._left_card_count);
            game_end.setShowBirdEffect(GRR._show_bird_effect == false ? 0 : 1);

            for (int i = 0; i < GameConstants.MAX_NIAO_CARD && i < GRR._count_niao; i++) {
                game_end.addCardsDataNiao(GRR._cards_data_niao[i]);
            }
            for (int i = 0; i < GameConstants.MAX_NIAO_CARD && i < GRR._count_niao_fei; i++) {
                game_end.addCardsDataNiao(GRR._cards_data_niao_fei[i]);
            }
            game_end.setCountPickNiao(GRR._count_pick_niao + GRR._count_pick_niao_fei);// 中鸟个数

            for (int i = 0; i < GameConstants.GAME_PLAYER; i++) {
                // 鸟牌，必须按四人计算
                Int32ArrayResponse.Builder pnc = Int32ArrayResponse.newBuilder();
                for (int j = 0; j < GRR._player_niao_count[i]; j++) {
                    pnc.addItem(GRR._player_niao_cards[i][j]);
                }
                for (int j = 0; j < GRR._player_niao_count_fei[i]; j++) {
                    pnc.addItem(GRR._player_niao_cards_fei[i][j]);
                }
                game_end.addPlayerNiaoCards(pnc);
            }

            for (int i = 0; i < getTablePlayerNumber(); i++) {
                game_end.addHuResult(GRR._hu_result[i]);
                Int32ArrayResponse.Builder hc = Int32ArrayResponse.newBuilder();
                for (int j = 0; j < GameConstants.MAX_COUNT; j++) {
                    hc.addItem(GRR._chi_hu_card[i][j]);
                }

                for (int h = 0; h < GRR._chi_hu_card[i].length; h++) {
                    game_end.addHuCardData(GRR._chi_hu_card[i][h]);
                }

                game_end.addHuCardArray(hc);
            }

            // 现在权值只有一位
            long rv[] = new long[GameConstants.MAX_RIGHT_COUNT];

            set_result_describe();

            for (int i = 0; i < getTablePlayerNumber(); i++) {
                GRR._card_count[i] = _logic.switch_to_cards_data(GRR._cards_index[i], GRR._cards_data[i]);

                Int32ArrayResponse.Builder cs = Int32ArrayResponse.newBuilder();
                for (int j = 0; j < GRR._card_count[i]; j++) {
                    cs.addItem(GRR._cards_data[i][j]);
                }
                game_end.addCardsData(cs);

                WeaveItemResponseArrayResponse.Builder weaveItem_array = WeaveItemResponseArrayResponse.newBuilder();
                for (int j = 0; j < GRR._weave_count[i]; j++) {
                    WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
                    weaveItem_item.setCenterCard(GRR._weave_items[i][j].center_card);
                    weaveItem_item.setProvidePlayer(GRR._weave_items[i][j].provide_player);
                    weaveItem_item.setPublicCard(GRR._weave_items[i][j].public_card);
                    weaveItem_item.setWeaveKind(GRR._weave_items[i][j].weave_kind);
                    weaveItem_array.addWeaveItem(weaveItem_item);
                }
                game_end.addWeaveItemArray(weaveItem_array);

                GRR._chi_hu_rights[i].get_right_data(rv);
                game_end.addChiHuRight(rv[0]);

                GRR._start_hu_right[i].get_right_data(rv);
                game_end.addStartHuRight(rv[0]);

                game_end.addProvidePlayer(GRR._provider[i]);
                game_end.addGameScore(GRR._game_score[i]);
                game_end.addGangScore(lGangScore[i]);
                game_end.addStartHuScore(GRR._start_hu_score[i]);
                game_end.addResultDes(GRR._result_des[i]);

                game_end.addWinOrder(GRR._win_order[i]);

                Int32ArrayResponse.Builder lfs = Int32ArrayResponse.newBuilder();
                for (int j = 0; j < getTablePlayerNumber(); j++) {
                    lfs.addItem(GRR._lost_fan_shu[i][j]);
                }

                game_end.addLostFanShu(lfs);

            }

        }

        boolean end = false;
        if (reason == GameConstants.Game_End_NORMAL || reason == GameConstants.Game_End_DRAW) {
            if (_cur_round >= _game_round && (!is_sys())) {
                end = true;
                game_end.setRoomOverType(1);
                game_end.setPlayerResult(process_player_result(reason));
            } else {
            }
        } else if ((!is_sys()) && (reason == GameConstants.Game_End_RELEASE_PLAY
                || reason == GameConstants.Game_End_RELEASE_NO_BEGIN || reason == GameConstants.Game_End_RELEASE_RESULT
                || reason == GameConstants.Game_End_RELEASE_PLAY_TIME_OUT
                || reason == GameConstants.Game_End_RELEASE_WAIT_TIME_OUT
                || reason == GameConstants.Game_End_RELEASE_SYSTEM)) {
            end = true;
            real_reason = GameConstants.Game_End_DRAW;
            game_end.setRoomOverType(1);
            game_end.setPlayerResult(process_player_result(reason));
        }
        game_end.setEndType(real_reason);

        roomResponse.setGameEnd(game_end);

        send_response_to_room(roomResponse);

        record_game_round(game_end);

        if (reason == GameConstants.Game_End_RELEASE_PLAY_TIME_OUT
                || reason == GameConstants.Game_End_RELEASE_WAIT_TIME_OUT) {
            for (int j = 0; j < getTablePlayerNumber(); j++) {
                Player player = get_players()[j];
                if (player == null)
                    continue;
                send_error_notify(j, 1, "游戏解散成功!");

            }
        }

        if (end && (!is_sys())) {
            PlayerServiceImpl.getInstance().delRoomId(getRoom_id());
        }

        if (!is_sys()) {
            GRR = null;
        }

        if (is_sys()) {
            clear_score_in_gold_room();
        }

        return false;
    }

    @Override
    public void process_chi_hu_player_operate(int seat_index, int operate_card, boolean rm) {
        ChiHuRight chr = GRR._chi_hu_rights[seat_index];
        int effect_count = chr.type_count;
        long effect_indexs[] = new long[effect_count];
        for (int i = 0; i < effect_count; i++) {
            effect_indexs[i] = chr.type_list[i];
        }

        operate_effect_action(seat_index, GameConstants.EFFECT_ACTION_TYPE_HU, effect_count, effect_indexs, 1,
                GameConstants.INVALID_SEAT);

        operate_player_cards(seat_index, 0, null, 0, null);

        if (rm) {
            GRR._cards_index[seat_index][_logic.switch_to_card_index(operate_card)]--;
        }

        int cards[] = new int[GameConstants.MAX_COUNT];
        int hand_card_count = _logic.switch_to_cards_data(GRR._cards_index[seat_index], cards);

        cards[hand_card_count] = operate_card + GameConstants.CARD_ESPECIAL_TYPE_HU;
        hand_card_count++;

        operate_show_card(seat_index, GameConstants.Show_Card_HU, hand_card_count, cards, GameConstants.INVALID_SEAT);

        return;
    }

    @Override
    public void process_chi_hu_player_score(int seat_index, int provide_index, int operate_card, boolean zimo) {
        GRR._chi_hu_card[seat_index][0] = operate_card;

        GRR._win_order[seat_index] = 1;

        ChiHuRight chr = GRR._chi_hu_rights[seat_index];

        countCardType(chr, seat_index);

        int di_fen = get_di_fen(chr, provide_index, seat_index);

        int lChiHuScore = di_fen * GameConstants.CELL_SCORE;

        if (zimo) {
            for (int i = 0; i < getTablePlayerNumber(); i++) {
                if (i == seat_index)
                    continue;

                GRR._lost_fan_shu[i][seat_index] = di_fen;
            }
        } else {
            GRR._lost_fan_shu[provide_index][seat_index] = di_fen;
        }

        // TODO: 总分=（底分 + 风坎数 + 断门数 + 相互之间的跑分）*（是否自摸）+ （相互之间的杠分）
        if (zimo) {
            for (int i = 0; i < getTablePlayerNumber(); i++) {
                if (i == seat_index)
                    continue;

                int s = lChiHuScore;

                if (i == GRR._banker_player)
                    s += 2;

                s += feng_kan;
                s += GRR._chi_hu_rights[seat_index].duanmen_count;
                s += _player_result.pao[seat_index] + _player_result.pao[provide_index];

                GRR._game_score[i] -= 2 * s;
                GRR._game_score[seat_index] += 2 * s;
            }
        } else {
            int s = lChiHuScore;
            s += feng_kan;
            s += GRR._chi_hu_rights[seat_index].duanmen_count;
            s += _player_result.pao[seat_index] + _player_result.pao[provide_index];

            GRR._game_score[provide_index] -= s;
            GRR._game_score[seat_index] += s;

            if (!GRR._chi_hu_rights[seat_index].opr_and(Constants_HuaXian.CHR_JIE_PAO).is_empty())
                GRR._chi_hu_rights[provide_index].opr_or(Constants_HuaXian.CHR_FANG_PAO);
        }
    }

    private int get_di_fen(ChiHuRight chr, int provide_index, int seat_index) {
        int di_fen = 1;
        if (!chr.opr_and(Constants_HuaXian.CHR_ZI_MO).is_empty()) {
            if (seat_index == GRR._banker_player) {
                di_fen = 4;
            } else {
                di_fen = 2;
            }
        } else {
            if (provide_index == GRR._banker_player || seat_index == GRR._banker_player) {
                di_fen = 4;
            } else {
                di_fen = 3;
            }
        }
        return di_fen;
    }

    @Override
    public boolean operate_player_cards_with_ting(int seat_index, int card_count, int cards[], int weave_count,
            WeaveItem weaveitems[]) {
        RoomResponse.Builder roomResponse = RoomResponse.newBuilder();
        roomResponse.setType(MsgConstants.RESPONSE_REFRESH_PLAYER_CARDS);
        roomResponse.setGameStatus(_game_status);
        roomResponse.setTarget(seat_index);
        roomResponse.setCardType(1);

        this.load_common_status(roomResponse);

        roomResponse.setCardCount(card_count);
        roomResponse.setWeaveCount(weave_count);
       
        if (weave_count > 0) {
            for (int j = 0; j < weave_count; j++) {
                WeaveItemResponse.Builder weaveItem_item = WeaveItemResponse.newBuilder();
                weaveItem_item.setProvidePlayer(weaveitems[j].provide_player);
                weaveItem_item.setPublicCard(weaveitems[j].public_card);
                weaveItem_item.setWeaveKind(weaveitems[j].weave_kind);
                weaveItem_item.setCenterCard(weaveitems[j].center_card);
                roomResponse.addWeaveItems(weaveItem_item);
            }
        }

        send_response_to_other(seat_index, roomResponse);

        for (int j = 0; j < card_count; j++) {
            roomResponse.addCardData(cards[j]);
        }

        int ting_count = _playerStatus[seat_index]._hu_out_card_count;

        roomResponse.setOutCardCount(ting_count);

        for (int i = 0; i < ting_count; i++) {
            int ting_card_cout = _playerStatus[seat_index]._hu_out_card_ting_count[i];
            roomResponse.addOutCardTingCount(ting_card_cout);
            
            roomResponse.addOutCardTing(
                    _playerStatus[seat_index]._hu_out_card_ting[i] + GameConstants.CARD_ESPECIAL_TYPE_TING);

            Int32ArrayResponse.Builder int_array = Int32ArrayResponse.newBuilder();
            for (int j = 0; j < ting_card_cout; j++) {
                int_array.addItem(_playerStatus[seat_index]._hu_out_cards[i][j]);
            }
            roomResponse.addOutCardTingCards(int_array);
        }

        this.send_response_to_player(seat_index, roomResponse);

        GRR.add_room_response(roomResponse);

        return true;
    }

    @Override
    protected void set_result_describe() {
        int chrTypes;
        long type = 0;

        int[] jie_gang = new int[getTablePlayerNumber()];
        int[] fang_gang = new int[getTablePlayerNumber()];
        int[] an_gang = new int[getTablePlayerNumber()];
        // TODO: 荒庄荒杠
        if (GRR._end_type != GameConstants.Game_End_DRAW) {
            for (int player = 0; player < getTablePlayerNumber(); player++) {
                if (GRR != null) {
                    for (int w = 0; w < GRR._weave_count[player]; w++) {
                        if (GRR._weave_items[player][w].weave_kind != GameConstants.WIK_GANG) {
                            continue;
                        }

                        if (GRR._weave_items[player][w].public_card == 0) {
                            an_gang[player]++;
                        } else {
                            jie_gang[player]++;
                            fang_gang[GRR._weave_items[player][w].provide_player]++;
                        }
                    }
                }
            }
        }

        for (int player = 0; player < getTablePlayerNumber(); player++) {
            StringBuilder result = new StringBuilder("");

            chrTypes = GRR._chi_hu_rights[player].type_count;

            for (int typeIndex = 0; typeIndex < chrTypes; typeIndex++) {
                type = GRR._chi_hu_rights[player].type_list[typeIndex];

                if (GRR._chi_hu_rights[player].is_valid()) {
                    if (type == Constants_HuaXian.CHR_ZI_MO)
                        result.append(" 自摸");

                    if (type == Constants_HuaXian.CHR_JIE_PAO)
                        result.append(" 接炮");
                } else if (type == Constants_HuaXian.CHR_FANG_PAO) {
                    result.append(" 放炮");
                }
            }

            if (GRR._chi_hu_rights[player].is_valid()) {
                if (feng_kan > 0)
                    result.append(" 风坎X" + feng_kan);
                if (GRR._chi_hu_rights[player].duanmen_count > 0)
                    result.append(" 断门X" + GRR._chi_hu_rights[player].duanmen_count);
            }

            if (_player_result.pao[player] > 0)
                result.append(" 下跑X" + _player_result.pao[player]);

            // TODO: 荒庄荒杠
            if (GRR._end_type != GameConstants.Game_End_DRAW) {
                if (an_gang[player] > 0) {
                    result.append(" 暗杠X" + an_gang[player]);
                }
                if (fang_gang[player] > 0) {
                    result.append(" 放杠X" + fang_gang[player]);
                }
                if (jie_gang[player] > 0) {
                    result.append(" 接杠X" + jie_gang[player]);
                }
            }

            int total_an_gang_score = 0;
            for (int i = 0; i < getTablePlayerNumber(); i++) {
                if (i == player)
                    continue;

                total_an_gang_score += an_gang[i] * 2;
            }

            int gang_fen = an_gang[player] * 6 + jie_gang[player] * 3 - fang_gang[player] * 3 - total_an_gang_score;
            result.append(" 杠分X" + gang_fen);

            GRR._result_des[player] = result.toString();
        }
    }

    @Override
    public boolean handler_requst_pao_qiang(Player player, int pao, int qiang) {
        return _handler_pao.handler_pao_qiang(this, player.get_seat_index(), pao, qiang);
    }

    @Override
    public boolean trustee_timer(int operate_id, int seat_index) {
        return true;
    }

    @Override
    public void test_cards() {
        int[] cards_of_player0 = new int[] { 0x01, 0x01, 0x01, 0x01, 0x05, 0x06, 0x07, 0x08, 0x09, 0x31, 0x32, 0x33,
                0x034 };
        int[] cards_of_player1 = new int[] { 0x01, 0x01, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x19, 0x15, 0x12, 0x23,
                0x17 };
        int[] cards_of_player3 = new int[] { 0x01, 0x01, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x19, 0x15, 0x12, 0x23,
                0x17 };
        int[] cards_of_player2 = new int[] { 0x01, 0x01, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x19, 0x15, 0x12, 0x23,
                0x17 };

        for (int i = 0; i < getTablePlayerNumber(); i++) {
            for (int j = 0; j < GameConstants.MAX_INDEX; j++) {
                GRR._cards_index[i][j] = 0;
            }
        }

        for (int j = 0; j < 13; j++) {
            if (getTablePlayerNumber() == 4) {
                GRR._cards_index[0][_logic.switch_to_card_index(cards_of_player0[j])] += 1;
                GRR._cards_index[1][_logic.switch_to_card_index(cards_of_player1[j])] += 1;
                GRR._cards_index[2][_logic.switch_to_card_index(cards_of_player2[j])] += 1;
                GRR._cards_index[3][_logic.switch_to_card_index(cards_of_player3[j])] += 1;
            } else if (getTablePlayerNumber() == 3) {
                GRR._cards_index[0][_logic.switch_to_card_index(cards_of_player0[j])] += 1;
                GRR._cards_index[1][_logic.switch_to_card_index(cards_of_player1[j])] += 1;
                GRR._cards_index[2][_logic.switch_to_card_index(cards_of_player2[j])] += 1;
            }
        }

        if (BACK_DEBUG_CARDS_MODE) {
            if (debug_my_cards != null) {
                if (debug_my_cards.length > 14) {
                    int[] temps = new int[debug_my_cards.length];
                    System.arraycopy(debug_my_cards, 0, temps, 0, temps.length);
                    testRealyCard(temps);
                    debug_my_cards = null;
                } else {
                    int[] temps = new int[debug_my_cards.length];
                    System.arraycopy(debug_my_cards, 0, temps, 0, temps.length);
                    testSameCard(temps);
                    debug_my_cards = null;
                }
            }
        }
    }
}
