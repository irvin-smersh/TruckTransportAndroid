package com.example.irvin.trucktransport.enums;

/**
 * Created by IvanX on 16.4.2017..
 */

public enum QueryType {
    REGISTRATION(1), LOGIN(2), LOGOUT(3), NALOG(4), ZADATAK_CHECK_IN(5), ZADATAK_CHECK_OUT(6), STAJALISTE_CHECK_IN(7),
    STAJALISTE_CHECK_OUT(9), ORDERS(10), ALERTS(11), EXCHANGES(12), NALOG_KRAJ(13), DATA_HISTORY(14),
    DATA_ASKS(15), DATA_BIDS(16), DATA_ALL(17), DATA_ORDERS(18), CHART_M(19), CHART_H(20),
    CHART_D(21), USERWATCHLIST(22), NEWSFEED(23), TICKER(24), ADDAPIKEY(27), DELETEAPIKEY(28),
    SIGNUP(29), GETTING_PASS(30), ACTIVATEAPIKEY(31), ACTIVATETRADINGKEY(32), GET_STATISTIKA(33),
    SAVEPREFS(34), UPDATEUSER(35), ADDORDER(36), CANCELORDER(37), DELETEALERT(38), ORDERTYPES(39)
    , UPDATETICKERS(40), NEW_MESSAGES(41), SEND_LOCATIONS(42), LAST_LOCATION(43), REPLY(44);
    public int value;

    private QueryType(int value) {
        this.value = value;
    }

    public static QueryType valueOf(int a) {
        switch (a) {
            case 1:
                return REGISTRATION;
            case 2:
                return LOGIN;
            case 3:
                return LOGOUT;
            case 4:
                return NALOG;
            case 5:
                return ZADATAK_CHECK_IN;
            case 6:
                return ZADATAK_CHECK_OUT;
            case 7:
                return STAJALISTE_CHECK_IN;
            case 9:
                return STAJALISTE_CHECK_OUT;
            case 10:
                return ORDERS;
            case 11:
                return ALERTS;
            case 12:
                return EXCHANGES;
            case 13:
                return NALOG_KRAJ;
            case 14:
                return DATA_HISTORY;
            case 15:
                return DATA_ASKS;
            case 16:
                return DATA_BIDS;
            case 17:
                return DATA_ALL;
            case 18:
                return DATA_ORDERS;
            case 19:
                return CHART_M;
            case 20:
                return CHART_H;
            case 21:
                return CHART_D;
            case 22:
                return USERWATCHLIST;
            case 24:
                return TICKER;
            case 25:
                return NEWSFEED;
            case 26:
                return NEWSFEED;
            case 27:
                return ADDAPIKEY;
            case 28:
                return DELETEAPIKEY;
            case 29:
                return SIGNUP;
            case 30:
                return GETTING_PASS;
            case 31:
                return ACTIVATEAPIKEY;
            case 32:
                return ACTIVATETRADINGKEY;
            case 33:
                return GET_STATISTIKA;
            case 34:
                return SAVEPREFS;
            case 35:
                return UPDATEUSER;
            case 36:
                return ADDORDER;
            case 37:
                return CANCELORDER;
            case 38:
                return DELETEALERT;
            case 39:
                return ORDERTYPES;
            case 40:
                return UPDATETICKERS;
            case 41:
                return NEW_MESSAGES;
            case 42:
                return SEND_LOCATIONS;
            case 43:
                return LAST_LOCATION;
            case 44:
                return REPLY;

            default:
                return null;
        }
    }
}