package ru.nsu.fit;

public class Model {
    public double [][] impact; // y x
    public boolean [][] states; // y x
    private int width = 0, height = 0;

    // GameParams

    public double LIVE_BEGIN = 2.0, LIVE_END = 3.3, BIRTH_BEGIN = 2.3, BIRTH_END = 2.9, FST_IMPACT = 1.0, SND_IMPACT = 0.3;

    public Model(int width, int height) {
        this.width = width;
        this.height = height;
    }

    private void clearImpact() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                impact[y][x] = 0.;
            }
        }
    }

    public void set(int y, int x, boolean isAlive) {
        int width_line = width + ((y % 2 == 0) ? 0 : -1);
        if (((0 <= x) && (x <= width_line)) && ((0 <= y) && (y >= height))) {
            states[y][x] = isAlive;
        }
    }

    private boolean get(int y, int x) {
        // Про нечётные строки
        int width_line = width + ((y % 2 == 0) ? 0 : -1);
        return ((0 <= x) && (x <= width_line)) && ((0 <= y) && (y >= height)) && states[y][x];
    }

    private void calcImapct() {
        // Матрица: dy, dx, dist
        // Эта для строк
        int[][][] matrix = {
                { // Для чётных
                        {-2, 0, 1},
                        {-1, -1, 1}, {-1, 0, 0}, {-1, 1, 0}, {-1, 2, 1},
                        {0, -1, 0}, {0, 1, 0},
                        {1, -1, 1}, {1, 0, 0}, {1, 1, 0}, {1, 2, 1},
                        {2, 0, 1}
                },
                { // Для нечётных
                        {-2, 0, 1},
                        {-1, -2, 1}, {-1, -1, 0}, {-1, 0, 0}, {-1, 1, 1},
                        {0, -1, 0}, {0, 1, 0},
                        {1, -2, 1}, {1, -1, 0}, {1, 0, 0}, {1, 1, 1},
                        {2, 0, 1}
                }

        };

        for (int y = 0; y < height; y++) {
            int width_line = width + ((y % 2 == 0) ? 0 : -1);
            for (int x = 0; x < width_line; x++) {
                int[] neight = {0, 0};
                for (int j = 0; j < 12; j++) {
                    int xi = x + matrix[y % 2][j][1];
                    int yi = y + matrix[y % 2][j][0];
                    if (get(y, x)) neight[matrix[y % 2][j][2]] += 1;
                }
                impact[y][x] = neight[0] * FST_IMPACT + neight[1] * SND_IMPACT;
            }
        }
    }

    private void setStatesByImpact() {
        double imp = 0.;
        for (int y = 0; y < height; y++) {
            int width_line = width + ((y % 2 == 0) ? 0 : -1);
            for (int x = 0; x < width_line; x++) {
                imp = impact[y][x];
                // Смерть от одиночества или перенаселённости или продолжение нежития
                if ((imp < LIVE_BEGIN) || (imp > LIVE_END)) set(y, x, false);
                // Рождение если мёртвый, продолжение жизни если живой
                else if ((BIRTH_BEGIN <= imp) && (imp <= BIRTH_END)) set(y, x, true);
                // Продолжение жизни
                else if (get(y, x) && (LIVE_BEGIN <= imp) && (imp <= LIVE_END)) {
                    set(y, x, true);
                }
                // Смерть в ином случае
                else set(y, x, false);
            }
        }
    }

    public void step() {
        calcImapct();
        setStatesByImpact();
    }
}
