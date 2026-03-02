package com.marchioro.brewer.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.marchioro.brewer.model.Permissao;
import com.marchioro.brewer.repository.PermissaoRepository;

@Component
public class PermissaoConverter implements Converter<String, Permissao> {

    private final PermissaoRepository permissaoRepository;

    public PermissaoConverter(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @Override
    public Permissao convert(String id) {

        if (id == null || id.isBlank()) {
            return null;
        }

        return permissaoRepository
                .findById(Long.valueOf(id))
                .orElse(null);
    }
}