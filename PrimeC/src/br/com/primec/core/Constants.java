package br.com.primec.core;

public interface Constants extends ScannerConstants, ParserConstants {

    int EPSILON = 0;
    int DOLLAR = 1;

    int t_TOKEN_2 = 2; //"("
    int t_TOKEN_3 = 3; //")"
    int t_TOKEN_4 = 4; //";"
    int t_TOKEN_5 = 5; //"{"
    int t_TOKEN_6 = 6; //"}"
    int t_TOKEN_7 = 7; //"["
    int t_TOKEN_8 = 8; //"]"
    int t_TOKEN_9 = 9; //","
    int t_TOKEN_10 = 10; //":"
    int t_TOKEN_11 = 11; //"."
    int t_TOKEN_12 = 12; //"="
    int t_HEX_VL = 13;
    int t_BIN_VL = 14;
    int t_INT_VL = 15;
    int t_DBL_VL = 16;
    int t_STR_VL = 17;
    int t_BOOL_VL = 18;
    int t_OP_MAIS = 19;
    int t_OP_MENOS = 20;
    int t_OP_VEZES = 21;
    int t_OP_DIVISAO = 22;
    int t_OP_GT = 23;
    int t_OP_LT = 24;
    int t_OP_EQ = 25;
    int t_OP_LTE = 26;
    int t_OP_GTE = 27;
    int t_OP_NEQ = 28;
    int t_OP_NOT = 29;
    int t_OP_AND = 30;
    int t_OP_OR = 31;
    int t_OP_LSHIFT = 32;
    int t_OP_RSHIFT = 33;
    int t_OP_BWS_AND = 34;
    int t_OP_BWS_OR = 35;
    int t_OP_BWS_XOR = 36;
    int t_OP_BWS_NOT = 37;
    int t_ID = 38;
    int t_MAIN = 39;
    int t_RETURN = 40;
    int t_CONST = 41;
    int t_VOID = 42;
    int t_HEX = 43;
    int t_BIN = 44;
    int t_INT = 45;
    int t_DBL = 46;
    int t_STR = 47;
    int t_IF = 48;
    int t_ELSE = 49;
    int t_WHILE = 50;
    int t_DO = 51;
    int t_FOR = 52;
    int t_INPUT = 53;
    int t_OUTPUT = 54;

}
