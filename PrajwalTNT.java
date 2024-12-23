import java.util.ArrayList;
import java.util.List;

public class PrajwalTNT 
{

    // Define the differential equation dy/dx = x / y
    public static double f(double x, double y) {
        return x / y;
    }

    // Runge-Kutta 4th Order (RK4) to compute initial values
    public static List<List<Double>> rk4(double x0, double y0, double h, int n) {
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        x.add(x0);
        y.add(y0);

        for (int i = 0; i < n; i++) {
            double k1 = h * f(x0, y0);
            double k2 = h * f(x0 + h / 2, y0 + k1 / 2);
            double k3 = h * f(x0 + h / 2, y0 + k2 / 2);
            double k4 = h * f(x0 + h, y0 + k3);

            y0 += (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            x0 += h;

            x.add(x0);
            y.add(y0);
        }

        List<List<Double>> result = new ArrayList<>();
        result.add(x);
        result.add(y);
        return result;
    }

    // Adams-Bashforth 2-step Method
    public static List<List<Double>> adamsBashforth(List<Double> x, List<Double> y, double h, int steps) {
        for (int i = x.size(); i < steps; i++) {
            double xPrev = x.get(x.size() - 2);
            double yPrev = y.get(y.size() - 2);

            double xLast = x.get(x.size() - 1);
            double yLast = y.get(y.size() - 1);

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

    public static void main(String[] args) {
        // Initial conditions
        double x0 = 1.0;
        double y0 = 3.0;
        double h = 0.2; // Step size
        double xEnd = 2.0; // Final value of x

        // Number of steps
        int nRK = 2; // RK4 will compute the first 2 points
        int steps = (int) ((xEnd - x0) / h) + 1;

        // Compute initial values using RK4
        List<List<Double>> rk4Results = rk4(x0, y0, h, nRK);
        List<Double> x = rk4Results.get(0);
        List<Double> y = rk4Results.get(1);

        // Apply Adams-Bashforth method
        List<List<Double>> adamsResults = adamsBashforth(x, y, h, steps);
        x = adamsResults.get(0);
        y = adamsResults.get(1);

        // Print results
        System.out.println("Numerical Solution (Adams-Bashforth):");
        for (int i = 0; i < x.size(); i++) {
            System.out.printf("x = %.2f, y = %.5f%n", x.get(i), y.get(i));
        }

        // Generate analytical solution and compare
        System.out.println("\nAnalytical Solution:");
        for (double xi = x0; xi <= xEnd; xi += 0.1) {
            double yi = analyticalSolution(xi);
            System.out.printf("x = %.2f, y = %.5f%n", xi, yi);
        }
    }
}
