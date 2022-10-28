package com.programmers.caculation;

import com.programmers.Service;
import com.programmers.caculation.dto.TokenizeRequestDto;
import com.programmers.caculation.dto.TokenizeResponseDto;
import com.programmers.caculation.model.NumberAndOperator;

import java.util.ArrayList;
import java.util.List;

public class TokenizeService implements Service {
    private final StringBuffer sb = new StringBuffer();
    private NumberAndOperator number;
    private NumberAndOperator op;
    TokenizeResponseDto tokenize(TokenizeRequestDto tokenizeRequestDto) throws Exception{
        String expression= tokenizeRequestDto.value;
        List<NumberAndOperator> tokenizedInput = new ArrayList<>();
        char[] expressionCharArray = expression.toCharArray();
        for (int i = 0; i < expressionCharArray.length; i++) {
            char c = expressionCharArray[i];

            switch (c) {
                case '*', '/', '+', '-' -> {//가장 앞에 오는 +, -는 통과하게, 가장 마지막이 연산자로 띁나면 실패하게
                    if (i == (expressionCharArray.length - 1)) {
                        throw new Exception("연산자는 가장 마지막에 올 수 없습니다.");
                    }
                    try {
                        number =  new NumberAndOperator(Double.parseDouble(sb.toString()));
                        tokenizedInput.add(number);
                        op = new NumberAndOperator(c);
                        tokenizedInput.add(op);
                        sb.delete(0, sb.length());
                    } catch (NumberFormatException e) {
                        if ((i == 0 && c == '+') || (i == 0 && c == '-')) {
                            sb.append(c);
                        } else {
                            throw new Exception("연산자는 연달아 두번 이상 올 수 없습니다.");
                        }
                    }
                }
                case '=' -> {
                    if (i == (expressionCharArray.length - 1)) {
                        number = new NumberAndOperator(Double.parseDouble(sb.toString()));
                        tokenizedInput.add(number);
                        sb.delete(0, sb.length());
                    } else {
                        throw new Exception("=의 뒤에는 숫자를 넣을 수 없습니다.");
                    }
                }
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.' -> {
                    sb.append(c);
                }
                default -> {
                    throw new Exception("잘못된 입력입니다.");
                }
            }
        }
        if (sb.length() != 0) {
            number = new NumberAndOperator(Double.parseDouble(sb.toString()));
            tokenizedInput.add(number);
            sb.delete(0, sb.length());
        }
        return new TokenizeResponseDto(tokenizedInput);
    }
}