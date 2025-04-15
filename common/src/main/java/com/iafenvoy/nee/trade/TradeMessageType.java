package com.iafenvoy.nee.trade;

public enum TradeMessageType {
    ANOTHER_ACCEPT, ANOTHER_CANCEL_ACCEPT, ANOTHER_CLOSE_SCREEN, SELF_ACCEPT, SELF_CANCEL_ACCEPT, SELF_CLOSE_SCREEN;
    public static final Runnable EMPTY = () -> {
    };
}
