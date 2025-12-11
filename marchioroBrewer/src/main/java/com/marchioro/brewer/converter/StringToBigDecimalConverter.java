package com.marchioro.brewer.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBigDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            // Converte vírgula para ponto para poder salvar corretamente
            DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(new Locale("pt", "BR"));
            df.setParseBigDecimal(true);

            return (BigDecimal) df.parse(value);

        } catch (ParseException e) {
            throw new IllegalArgumentException("Valor inválido para BigDecimal: " + value, e);
        }
    }
}
