/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Python Addin for Oracle9i JDeveloper.
 *
 * The Initial Developer of the Original Code is
 * Brian Duff (Brian.Duff@dubh.org).
 * Portions created by the Initial Developer are Copyright (C) 2002
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */

package org.dubh.jdev.parser.python;


public interface PythonTokens
{
  int EOF = 0;
  int SPACE = 1;
  int CONTINUATION = 4;
  int NEWLINE1 = 5;
  int NEWLINE = 6;
  int NEWLINE2 = 7;
  int NEWLINE3 = 8;
  int CRLF1 = 12;
  int DEDENT = 14;
  int INDENT = 15;
  int TRAILING_COMMENT = 16;
  int SINGLE_LINE_COMMENT = 17;
  int LPAREN = 18;
  int RPAREN = 19;
  int LBRACE = 20;
  int RBRACE = 21;
  int LBRACKET = 22;
  int RBRACKET = 23;
  int SEMICOLON = 24;
  int COMMA = 25;
  int DOT = 26;
  int COLON = 27;
  int PLUS = 28;
  int MINUS = 29;
  int MULTIPLY = 30;
  int DIVIDE = 31;
  int POWER = 32;
  int LSHIFT = 33;
  int RSHIFT = 34;
  int MODULO = 35;
  int NOT = 36;
  int XOR = 37;
  int OR = 38;
  int AND = 39;
  int EQUAL = 40;
  int GREATER = 41;
  int LESS = 42;
  int EQEQUAL = 43;
  int EQLESS = 44;
  int EQGREATER = 45;
  int LESSGREATER = 46;
  int NOTEQUAL = 47;
  int PLUSEQ = 48;
  int MINUSEQ = 49;
  int MULTIPLYEQ = 50;
  int DIVIDEEQ = 51;
  int MODULOEQ = 52;
  int ANDEQ = 53;
  int OREQ = 54;
  int XOREQ = 55;
  int LSHIFTEQ = 56;
  int RSHIFTEQ = 57;
  int POWEREQ = 58;
  int OR_BOOL = 59;
  int AND_BOOL = 60;
  int NOT_BOOL = 61;
  int IS = 62;
  int IN = 63;
  int LAMBDA = 64;
  int IF = 65;
  int ELSE = 66;
  int ELIF = 67;
  int WHILE = 68;
  int FOR = 69;
  int TRY = 70;
  int EXCEPT = 71;
  int DEF = 72;
  int CLASS = 73;
  int FINALLY = 74;
  int PRINT = 75;
  int PASS = 76;
  int BREAK = 77;
  int CONTINUE = 78;
  int RETURN = 79;
  int IMPORT = 80;
  int FROM = 81;
  int DEL = 82;
  int RAISE = 83;
  int GLOBAL = 84;
  int EXEC = 85;
  int ASSERT = 86;
  int AS = 87;
  int NAME = 88;
  int LETTER = 89;
  int DECNUMBER = 90;
  int HEXNUMBER = 91;
  int OCTNUMBER = 92;
  int FLOAT = 93;
  int EXPONENT = 94;
  int DIGIT = 95;
  int SINGLE_STRING = 100;
  int SINGLE_STRING2 = 101;
  int TRIPLE_STRING = 102;
  int TRIPLE_STRING2 = 103;

  int DEFAULT = 0;
  int FORCE_NEWLINE1 = 1;
  int FORCE_NEWLINE2 = 2;
  int FORCE_NEWLINE = 3;
  int INDENTING = 4;
  int INDENTATION_UNCHANGED = 5;
  int UNREACHABLE = 6;
  int IN_STRING11 = 7;
  int IN_STRING21 = 8;
  int IN_STRING13 = 9;
  int IN_STRING23 = 10;

  int COMMENT = 500;
  int FLOAT_LITERAL = 501;
  int STRING_LITERAL = 502;
  int INT_LITERAL = 503;
  int IMAGINARY_LITERAL = 504;
  int IDENTIFIER = 505;

  int KW_AND = 600;
  int KW_DEL = 601;
  int KW_FOR = 602;
  int KW_IS = 603;
  int KW_RAISE = 604;
  int KW_ASSERT = 605;
  int KW_ELIF = 606;
  int KW_FROM = 607;
  int KW_LAMBDA = 608;
  int KW_RETURN = 609;
  int KW_BREAK = 610;
  int KW_ELSE = 611;
  int KW_GLOBAL = 612;
  int KW_NOT = 613;
  int KW_TRY = 614;
  int KW_CLASS = 615;
  int KW_EXCEPT = 616;
  int KW_IF = 617;
  int KW_OR = 618; 
  int KW_WHILE = 619; 
  int KW_CONTINUE = 620; 
  int KW_EXEC = 621; 
  int KW_IMPORT = 622;
  int KW_PASS = 623;
  int KW_YIELD = 624;
  int KW_DEF = 625;
  int KW_FINALLY = 626;
  int KW_IN = 627; 
  int KW_PRINT = 628;
}