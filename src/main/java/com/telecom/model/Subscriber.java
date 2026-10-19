package com.telecom.model;

public class Subscriber {

    private int subscriberId;
    private String subscriberName;
    private String phoneNumber;
    private String email;
    private String subscriptionType;
    private String dataPlan;
    private double monthlyDataLimit;
    private double currentDataUsage;
    private String status;

    public Subscriber() {
    }

    public Subscriber(
            int subscriberId,
            String subscriberName,
            String phoneNumber,
            String email,
            String subscriptionType,
            String dataPlan,
            double monthlyDataLimit,
            double currentDataUsage,
            String status) {

        this.subscriberId = subscriberId;
        this.subscriberName = subscriberName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.subscriptionType = subscriptionType;
        this.dataPlan = dataPlan;
        this.monthlyDataLimit = monthlyDataLimit;
        this.currentDataUsage = currentDataUsage;
        this.status = status;
    }

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public String getDataPlan() {
        return dataPlan;
    }

    public void setDataPlan(String dataPlan) {
        this.dataPlan = dataPlan;
    }

    public double getMonthlyDataLimit() {
        return monthlyDataLimit;
    }

    public void setMonthlyDataLimit(double monthlyDataLimit) {
        this.monthlyDataLimit = monthlyDataLimit;
    }

    public double getCurrentDataUsage() {
        return currentDataUsage;
    }

    public void setCurrentDataUsage(double currentDataUsage) {
        this.currentDataUsage = currentDataUsage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}