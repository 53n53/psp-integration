package org.demo.psp.customizer;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;

import jakarta.inject.Singleton;

@Singleton
public class EnumErrorCustomizer extends DeserializationProblemHandler {

    @Override
    public Object handleInstantiationProblem(DeserializationContext ctxt, Class<?> instClass, Object argument, Throwable t) throws IOException {
        // change jackson deserialization behaviour when there's an error while deserializing an Enum
        // the objective is to have a nice error message when an invalid enum value is sent to the API
        if (instClass.isEnum() && t instanceof IllegalArgumentException) {
            final String badValue = argument == null ? "null" : argument.toString();
            final String allowed = Arrays.toString(instClass.getEnumConstants());
            final String enumSuffix = "EnumDTO";
            String enumClassName = instClass.getSimpleName();
            if (enumClassName.endsWith(enumSuffix)) {
                enumClassName = enumClassName.substring(0, enumClassName.length() - enumSuffix.length());
            }

            throw JsonMappingException.from(ctxt.getParser(),
                    "Invalid " + enumClassName + ": '" + badValue + "'. Allowed values: " + allowed, t);
        }
        // default behaviour
        return super.handleInstantiationProblem(ctxt, instClass, argument, t);
    }

}
