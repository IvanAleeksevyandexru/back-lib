package ru.gosuslugi.pgu.dto.descriptor.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValueDefinition {

    private String expression1 = "";
    private String expression2 = "";
    private String expression3 = "";
    private String expression4 = "";
    private String expression5 = "";
    private String expression6 = "";
    private String expression7 = "";
    private String expression8 = "";
    private String expression9 = "";
    private String expression10 = "";

    private String arg1 = "";
    private String arg2 = "";
    private String arg3 = "";
    private String arg4 = "";
    private String arg5 = "";
    private String arg6 = "";
    private String arg7 = "";
    private String arg8 = "";
    private String arg9 = "";
    private String arg10 = "";

    @JsonIgnore
    public List<String> getExpressions() {
        return List.of(
                expression1, expression2, expression3, expression4, expression5,
                expression6, expression7, expression8, expression9, expression10
        );
    }

    @JsonIgnore
    public List<String> getArgs(){
        return List.of(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    }
}
