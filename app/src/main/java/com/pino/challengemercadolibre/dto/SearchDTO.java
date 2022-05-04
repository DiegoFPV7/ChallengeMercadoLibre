package com.pino.challengemercadolibre.dto;

import java.util.List;

public class SearchDTO {
    private String site_id;
    private List<ResultDTO> results;

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public List<ResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultDTO> results) {
        this.results = results;
    }
}
