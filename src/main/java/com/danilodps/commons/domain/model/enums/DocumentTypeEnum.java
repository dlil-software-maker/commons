package com.danilodps.commons.domain.model.enums;

public enum DocumentTypeEnum {

    CPF(1L, "CPF", "Pessoa física"),
    CNPJ(2L, "CNPJ", "Pessoa jurídica");

    private final Long id;
    private final String shortName;
    private final String description;

    DocumentTypeEnum(Long id, String shortName, String description) {
        this.id = id;
        this.shortName = shortName;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getDescription() {
        return description;
    }

}