package CustomEngineClasses;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.io.Serializable;

public class Vec21 implements Serializable {
    private static final long serialVersionUID = 1L;
    public double x;
    public double y;

    public Vec21() {
        this(0.0F, 0.0F);
    }

    public Vec21(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec21(Vec21 toCopy) {
        this(toCopy.x, toCopy.y);
    }

    public final void setZero() {
        this.x = 0.0F;
        this.y = 0.0F;
    }

    public final Vec21 set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public final Vec21 set(Vec21 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public final Vec21 add(Vec21 v) {
        return new Vec21(this.x + v.x, this.y + v.y);
    }

    public final Vec21 sub(Vec21 v) {
        return new Vec21(this.x - v.x, this.y - v.y);
    }

    public final Vec21 mul(double a) {
        return new Vec21(this.x * a, this.y * a);
    }

    public final Vec21 negate() {
        return new Vec21(-this.x, -this.y);
    }

    public final Vec21 negateLocal() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public final Vec21 addLocal(Vec21 v) {
        this.x += v.x;
        this.y += v.y;
        return this;
    }

    public final Vec21 addLocal(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public final Vec21 subLocal(Vec21 v) {
        this.x -= v.x;
        this.y -= v.y;
        return this;
    }

    public final Vec21 mulLocal(double a) {
        this.x *= a;
        this.y *= a;
        return this;
    }

    public final Vec21 skew() {
        return new Vec21(-this.y, this.x);
    }

    public final void skew(Vec21 out) {
        out.x = -this.y;
        out.y = this.x;
    }

    public final double length() {
        return MathUtilsDub.sqrt(this.x * this.x + this.y * this.y);
    }

    public final double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public final double normalize() {
        double length = this.length();
        if (length < 1.1920929E-7F) {
            return 0.0F;
        } else {
            double invLength = 1.0F / length;
            this.x *= invLength;
            this.y *= invLength;
            return length;
        }
    }

    public final boolean isValid() {
        return !Double.isNaN(this.x) && !Double.isInfinite(this.x) && !Double.isNaN(this.y) && !Double.isInfinite(this.y);
    }

    public final Vec21 abs() {
        return new Vec21(MathUtilsDub.abs(this.x), MathUtilsDub.abs(this.y));
    }

    public final void absLocal() {
        this.x = MathUtilsDub.abs(this.x);
        this.y = MathUtilsDub.abs(this.y);
    }

    public final Vec21 clone() {
        return new Vec21(this.x, this.y);
    }

    public final String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

    public static final Vec21 abs(Vec21 a) {
        return new Vec21(MathUtilsDub.abs(a.x), MathUtilsDub.abs(a.y));
    }

    public static final void absToOut(Vec21 a, Vec21 out) {
        out.x = MathUtilsDub.abs(a.x);
        out.y = MathUtilsDub.abs(a.y);
    }

    public static final double dot(Vec21 a, Vec21 b) {
        return a.x * b.x + a.y * b.y;
    }

    public static final double cross(Vec21 a, Vec21 b) {
        return a.x * b.y - a.y * b.x;
    }

    public static final Vec21 cross(Vec21 a, double s) {
        return new Vec21(s * a.y, -s * a.x);
    }

    public static final void crossToOut(Vec21 a, double s, Vec21 out) {
        double tempy = -s * a.x;
        out.x = s * a.y;
        out.y = tempy;
    }

    public static final void crossToOutUnsafe(Vec21 a, double s, Vec21 out) {
        assert out != a;

        out.x = s * a.y;
        out.y = -s * a.x;
    }

    public static final Vec21 cross(double s, Vec21 a) {
        return new Vec21(-s * a.y, s * a.x);
    }

    public static final void crossToOut(double s, Vec21 a, Vec21 out) {
        double tempY = s * a.x;
        out.x = -s * a.y;
        out.y = tempY;
    }

    public static final void crossToOutUnsafe(double s, Vec21 a, Vec21 out) {
        assert out != a;

        out.x = -s * a.y;
        out.y = s * a.x;
    }

    public static final void negateToOut(Vec21 a, Vec21 out) {
        out.x = -a.x;
        out.y = -a.y;
    }

    public static final Vec21 min(Vec21 a, Vec21 b) {
        return new Vec21(a.x < b.x ? a.x : b.x, a.y < b.y ? a.y : b.y);
    }

    public static final Vec21 max(Vec21 a, Vec21 b) {
        return new Vec21(a.x > b.x ? a.x : b.x, a.y > b.y ? a.y : b.y);
    }

    public static final void minToOut(Vec21 a, Vec21 b, Vec21 out) {
        out.x = a.x < b.x ? a.x : b.x;
        out.y = a.y < b.y ? a.y : b.y;
    }

    public static final void maxToOut(Vec21 a, Vec21 b, Vec21 out) {
        out.x = a.x > b.x ? a.x : b.x;
        out.y = a.y > b.y ? a.y : b.y;
    }

    public int hashCode() {//never used this so skipping float -> double conversion
        int prime = 31;
        int result = 1;
        result = 31 * result + Float.floatToIntBits((float)this.x);
        result = 31 * result + Float.floatToIntBits((float)this.y);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Vec21 other = (Vec21)obj;
            if (Float.floatToIntBits((float)this.x) != Float.floatToIntBits((float)other.x)) {
                return false;
            } else {
                return Float.floatToIntBits((float)this.y) == Float.floatToIntBits((float)other.y);
            }
        }
    }
}
