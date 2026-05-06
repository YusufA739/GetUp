//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package CustomEngineClasses;

import org.jbox2d.common.Settings;

import java.util.Random;

public class MathUtilsDub extends PlatformMathUtilsDub {
    public static final double PI = (double)Math.PI;
    public static final double TWOPI = ((double)Math.PI * 2F);
    public static final double INV_PI = 0.31830987F;
    public static final double HALF_PI = ((double)Math.PI / 2F);
    public static final double QUARTER_PI = ((double)Math.PI / 4F);
    public static final double THREE_HALVES_PI = ((double)Math.PI * 1.5F);
    public static final double DEG2RAD = ((double)Math.PI / 180F);
    public static final double RAD2DEG = (180F / (double)Math.PI);
    public static final double[] sinLUT;

    public static final double sin(double x) {
        return Settings.SINCOS_LUT_ENABLED ? sinLUT(x) : (double)StrictMath.sin((double)x);
    }

    public static final double sinLUT(double x) {
        x %= ((double)Math.PI * 2F);
        if (x < 0.0F) {
            x += ((double)Math.PI * 2F);
        }

        if (Settings.SINCOS_LUT_LERP) {
            x /= 1.1E-4F;
            double index = x;
            if (index != 0) {
                x %= index;
            }

            return index == Settings.SINCOS_LUT_LENGTH - 1 ? (1.0F - x) * sinLUT[(int)index] + x * sinLUT[0] : (1.0F - x) * sinLUT[(int)index] + x * sinLUT[(int)index + 1];
        } else {
            return sinLUT[(int)round(x / 1.1E-4F) % Settings.SINCOS_LUT_LENGTH];
        }
    }

    public static final double cos(double x) {
        return Settings.SINCOS_LUT_ENABLED ? sinLUT(((double)Math.PI / 2F) - x) : (double)StrictMath.cos((double)x);
    }

    public static final double abs(double x) {
        if (Settings.FAST_ABS) {
            return x > 0.0F ? x : -x;
        } else {
            return Math.abs(x);
        }
    }

    public static final int abs(int x) {
        int y = x >> 31;
        return (x ^ y) - y;
    }

    public static final int floor(double x) {
        if (Settings.FAST_FLOOR) {
            int y = (int)x;
            return x < 0.0F && x != (double)y ? y - 1 : y;
        } else {
            return (int)Math.floor((double)x);
        }
    }

    public static final int ceil(double x) {
        if (Settings.FAST_CEIL) {
            int y = (int)x;
            return x > 0.0F && x != (double)y ? y + 1 : y;
        } else {
            return (int)Math.ceil((double)x);
        }
    }

    public static final long round(double x) {
        return Settings.FAST_ROUND ? floor(x + 0.5F) : StrictMath.round(x);
    }

    public static final int ceilPowerOf2(int x) {
        int pow2;
        for(pow2 = 1; pow2 < x; pow2 <<= 1) {
        }

        return pow2;
    }

    public static final double max(double a, double b) {
        return a > b ? a : b;
    }

    public static final int max(int a, int b) {
        return a > b ? a : b;
    }

    public static final double min(double a, double b) {
        return a < b ? a : b;
    }

    public static final int min(int a, int b) {
        return a < b ? a : b;
    }

    public static final double map(double val, double fromMin, double fromMax, double toMin, double toMax) {
        double mult = (val - fromMin) / (fromMax - fromMin);
        double res = toMin + mult * (toMax - toMin);
        return res;
    }

    public static final double clamp(double a, double low, double high) {
        return max(low, min(a, high));
    }

    public static final Vec21 clamp(Vec21 a, Vec21 low, Vec21 high) {
        Vec21 min = new Vec21();
        min.x = a.x < high.x ? a.x : high.x;
        min.y = a.y < high.y ? a.y : high.y;
        min.x = low.x > min.x ? low.x : min.x;
        min.y = low.y > min.y ? low.y : min.y;
        return min;
    }

    public static final void clampToOut(Vec21 a, Vec21 low, Vec21 high, Vec21 dest) {
        dest.x = a.x < high.x ? a.x : high.x;
        dest.y = a.y < high.y ? a.y : high.y;
        dest.x = low.x > dest.x ? low.x : dest.x;
        dest.y = low.y > dest.y ? low.y : dest.y;
    }

    public static final int nextPowerOfTwo(int x) {
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }

    public static final boolean isPowerOfTwo(int x) {
        return x > 0 && (x & x - 1) == 0;
    }

    public static final double atan2(double y, double x) {
        return Settings.FAST_ATAN2 ? fastAtan2(y, x) : (double)StrictMath.atan2((double)y, (double)x);
    }

    public static final double fastAtan2(double y, double x) {
        if (x == 0.0F) {
            if (y > 0.0F) {
                return ((double)Math.PI / 2F);
            } else {
                return y == 0.0F ? 0.0F : (-(double)Math.PI / 2F);
            }
        } else {
            double z = y / x;
            double atan;
            if (abs(z) < 1.0F) {
                atan = z / (1.0F + 0.28F * z * z);
                if (x < 0.0F) {
                    if (y < 0.0F) {
                        return atan - (double)Math.PI;
                    }

                    return atan + (double)Math.PI;
                }
            } else {
                atan = ((double)Math.PI / 2F) - z / (z * z + 0.28F);
                if (y < 0.0F) {
                    return atan - (double)Math.PI;
                }
            }

            return atan;
        }
    }

    public static final double reduceAngle(double theta) {
        theta %= ((double)Math.PI * 2F);
        if (abs(theta) > (double)Math.PI) {
            theta -= ((double)Math.PI * 2F);
        }

        if (abs(theta) > ((double)Math.PI / 2F)) {
            theta = (double)Math.PI - theta;
        }

        return theta;
    }

    public static final double randomFloat(double argLow, double argHigh) {
        return (double)Math.random() * (argHigh - argLow) + argLow;
    }

    public static final double randomFloat(Random r, double argLow, double argHigh) {
        return r.nextFloat() * (argHigh - argLow) + argLow;
    }

    public static final double sqrt(double x) {
        return (double)StrictMath.sqrt((double)x);
    }

    public static final double distanceSquared(Vec21 v1, Vec21 v2) {
        double dx = v1.x - v2.x;
        double dy = v1.y - v2.y;
        return dx * dx + dy * dy;
    }

    public static final double distance(Vec21 v1, Vec21 v2) {
        return sqrt(distanceSquared(v1, v2));
    }

    static {
        sinLUT = new double[Settings.SINCOS_LUT_LENGTH];

        for(int i = 0; i < Settings.SINCOS_LUT_LENGTH; ++i) {
            sinLUT[i] = (double)Math.sin((double)((double)i * 1.1E-4F));
        }

    }
}
