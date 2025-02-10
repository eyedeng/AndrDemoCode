package com.example.andrdemocode.cast;

/**
 * @author dengyan
 * @date 2025/1/17
 * @desc 封装虚拟屏相关配置参数
 */
public class DisplayConfig {
    private int width;
    private int height;
    private int density;
    private int frameRate;
    private int bitRate;
    private int mirrorMode;

    private DisplayConfig(int width, int height, int density, int frameRate, int bitRate, int mirrorMode) {
        this.width = width;
        this.height = height;
        this.density = density;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
        this.mirrorMode = mirrorMode;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDensity() {
        return density;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public int getBitRate() {
        return bitRate;
    }

    public boolean isMirror() {
        return mirrorMode == 0;
    }

    public static class Builder {
        private int width;
        private int height;
        private int density;
        private int frameRate = 30;
        private int bitRate = 6_000_000;
        private int mirrorMode;

        // 编码前宽高需要对齐
        private int align(int num) {
//            return (num + 15) / 16 * 16;
            return num & 0x7ffffffe;
        }

        public Builder setWidth(int width) {
            this.width = align(width);
            return this;
        }

        public Builder setHeight(int height) {
            this.height = align(height);
            return this;
        }

        public Builder setDensity(int density) {
            this.density = density;
            return this;
        }

        public Builder setFrameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public Builder setBitRate(int bitRate) {
            this.bitRate = bitRate;
            return this;
        }

        public Builder setMirrorMode(int mirrorMode) {
            this.mirrorMode = mirrorMode;
            return this;
        }

        public DisplayConfig build() {
            return new DisplayConfig(width, height, density, frameRate, bitRate, mirrorMode);
        }
    }
}
