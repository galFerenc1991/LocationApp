package com.example.locationapp.presentation.base;

public interface BaseView<T extends BasePresenter> {
    void initPresenter();

    void showProgressMain();

    void hideProgress();
}