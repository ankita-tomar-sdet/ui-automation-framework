@allure.label.layer:UI
@allure.label.owner:Ankita
Feature: User Login Functionality
  As a user, I want to be able to log in to the application
  so that I can access my personalized dashboard.

  Background:
    Given the user is on the login page

 @allure.label.severity:critical
 @allure.label.tms:TC-101
  Scenario: Login with valid credentials
    When the user enters a valid username and password and Clicks on Submit
    Then the user should see the dashboard page title "Automation Exercise"

 @allure.label.severity:critical
  @allure.label.tms:TC-102 
  Scenario: Login with invalid credentials
    When the user enters "invalid_user@gmai.com" and "wrong_password"
    Then the user should see an error message "Your email or password is incorrect!"