package com.marchioro.brewer.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuItem {

    private String nome;
    private String url;
    private String icone;
    private List<MenuItem> filhos = new ArrayList<>();

    public MenuItem(String nome, String url, String icone) {
        this.nome = nome;
        this.url = url;
        this.icone = icone;
    }

    public void addFilho(MenuItem filho) {
        this.filhos.add(filho);
    }
    
    public List<MenuItem> getFilhos() {
        return filhos;
    }

    public boolean temFilhos() {
        return !filhos.isEmpty();
    }
    
    // getters
    
	public String getNome() {
		return nome;
	}

	public String getUrl() {
		return url;
	}

	public String getIcone() {
		return icone;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public void setFilhos(List<MenuItem> filhos) {
		this.filhos = filhos;
	}   
}