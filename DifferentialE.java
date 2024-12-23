import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class DifferentialE extends Application {

    // Define the differential equation dy/dx = x / y
    public static double f(double x, double y) {
        return x / y;
    }

    // Adams-Bashforth 2-step Method
    public static List<List<Double>> adamsBashforth(double x0, double y0, double h, int steps) {
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();

        // Initialize the first two points using Euler's Method
        x.add(x0);
        y.add(y0);

        double x1 = x0 + h;
        double y1 = y0 + h * f(x0, y0);
        x.add(x1);
        y.add(y1);

        for (int i = 2; i < steps; i++) {
            double xPrev = x.get(i - 2);
            double yPrev = y.get(i - 2);

            double xLast = x.get(i - 1);
            double yLast = y.get(i - 1);

            double yp = yLast + h * (1.5 * f(xLast, yLast) - 0.5 * f(xPrev, yPrev));
            double xNext = xLast + h;

            x.add(xNext);
            y.add(yp);
        }

        List<List<Double>> result = new ArrayList<>();
        result.add(x);
        result.add(y);
        return result;
    }

    // Analytical solution
    public static double analyticalSolution(double x) {
        return Math.sqrt(x * x + 8);
    }

    @Override
    public void start(Stage stage) {
        // Initial conditions
        double x0 = 1.0;
        double y0 = 3.0;
        double h = 0.2; // Step size
        double xEnd = 2.0; // Final value of x

        // Number of steps
        int steps = (int) ((xEnd - x0) / h) + 1;

        // Apply Adams-Bashforth method
        List<List<Double>> adamsResults = adamsBashforth(x0, y0, h, steps);
        List<Double> x = adamsResults.get(0);
        List<Double> y = adamsResults.get(1);

        // Create the axes
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("y");

        // Create the chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Numerical vs Analytical Solutions");

        // Add the numerical solution
        XYChart.Series<Number, Number> numericalSeries = new XYChart.Series<>();
        numericalSeries.setName("Numerical Solution (Adams-Bashforth)");
        for (int i = 0; i < x.size(); i++) {
            numericalSeries.getData().add(new XYChart.Data<>(x.get(i), y.get(i)));
        }

        // Add the analytical solution
        XYChart.Series<Number, Number> analyticalSeries = new XYChart.Series<>();
        analyticalSeries.setName("Analytical Solution");
        for (double xi = x0; xi <= xEnd; xi += 0.01) {
            double yi = analyticalSolution(xi);
            analyticalSeries.getData().add(new XYChart.Data<>(xi, yi));
        }

        // Add series to chart
        lineChart.getData().addAll(numericalSeries, analyticalSeries);

        // Set up the scene
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Differential Equation Solver");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
