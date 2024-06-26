package nl.rikp.customerService.model;

import lombok.Getter;

import java.util.Objects;

/**
 * Enum representing the preferred measurement metrics.
 * Each metric is associated with a name.
 */
@Getter
public enum PreferredMetric {
    METRIC("Metric"),
    IMPERIAL("Imperial");

    private final String metricName;

    PreferredMetric(String metricName) {
        this.metricName = metricName;
    }

    /**
     * Converts a string representation of a metric to the corresponding PreferredMetric enum.
     *
     * @param metricName the name of the metric
     * @return the matching PreferredMetric
     * @throws IllegalArgumentException if the metricName does not match any preferred metric
     */
    public static PreferredMetric fromString(String metricName) {
        for (PreferredMetric metric : PreferredMetric.values()) {
            if (metric.metricName.equalsIgnoreCase(metricName)) {
                return metric;
            }
        }
        throw new IllegalArgumentException(
                String.format("Invalid metric name: '%s'. Supported metrics are: %s",
                        metricName,
                        String.join(", ", getSupportedMetricNames()))
        );
    }

    /**
     * Gets the supported metric names.
     *
     * @return an array of supported metric names
     */
    private static String[] getSupportedMetricNames() {
        return new String[] {
                METRIC.metricName, IMPERIAL.metricName
        };
    }

    /**
     * Converts pounds to kilograms.
     *
     * @param pounds the weight in pounds
     * @return the weight in kilograms
     */
    public static double toKilograms(double pounds) {
        return pounds * 0.453592;
    }

    /**
     * Converts kilograms to pounds.
     *
     * @param kilograms the weight in kilograms
     * @return the weight in pounds
     */
    public static double toPounds(double kilograms) {
        return kilograms * 2.20462;
    }

    /**
     * Converts inches to centimeters.
     *
     * @param inches the length in inches
     * @return the length in centimeters
     */
    public static double toCentimeters(double inches) {
        return inches * 2.54;
    }

    /**
     * Converts centimeters to inches.
     *
     * @param centimeters the length in centimeters
     * @return the length in inches
     */
    public static double toInches(double centimeters) {
        return centimeters * 0.393701;
    }

    /**
     * Converts gallons to liters.
     *
     * @param gallons the volume in gallons
     * @return the volume in liters
     */
    public static double toLiters(double gallons) {
        return gallons * 3.78541;
    }

    /**
     * Converts liters to gallons.
     *
     * @param liters the volume in liters
     * @return the volume in gallons
     */
    public static double toGallons(double liters) {
        return liters * 0.264172;
    }

    /**
     * Converts Fahrenheit to Celsius.
     *
     * @param fahrenheit the temperature in Fahrenheit
     * @return the temperature in Celsius
     */
    public static double toCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    /**
     * Converts Celsius to Fahrenheit.
     *
     * @param celsius the temperature in Celsius
     * @return the temperature in Fahrenheit
     */
    public static double toFahrenheit(double celsius) {
        return (celsius * 9 / 5) + 32;
    }

    /**
     * Determines the default metric for a given language.
     *
     * @param language the language
     * @return the default PreferredMetric for the language
     */
    public static PreferredMetric getDefaultMetricForLanguage(Language language) {
        if (Objects.requireNonNull(language) == Language.US) {
            return PreferredMetric.IMPERIAL;
        }
        return PreferredMetric.METRIC;
    }
}
