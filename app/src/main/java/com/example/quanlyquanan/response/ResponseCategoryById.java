package com.example.quanlyquanan.response;

import com.example.quanlyquanan.model.Category;

public class ResponseCategoryById {
    private String message, status, error;
    private Category category;

    public ResponseCategoryById(String message, String status, String error, Category category) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.category = category;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
