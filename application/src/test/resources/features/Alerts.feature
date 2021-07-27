Feature: Example

  Scenario: Alerts Summary - GET reference alerts
    Given existing 'Alerts Reference' in BPS
    When ISS receives a request on 'Alerts Reference' endpoint
    And the system returns status code 200
    Then ISS returns all alerts references

  Scenario: Alerts - Get alerts
    Given user details
    Given existing 'Alerts' in BPS
    When ISS receives a request on 'Alerts' endpoint with query params:
      | offset     | 5    |
      | limit      | 10   |
      | priorities | high |
      | dateFrom   | 10   |
      | dateTo     | 10   |
    Then the system returns status code 200