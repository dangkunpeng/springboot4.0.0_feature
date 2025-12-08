package com.sam.vt.signin;

public class BitMap {
    private byte[] bits;  // 存储位图
    private int size;     // 位图大小（位数）
    
    /**
     * 初始化BitMap
     * @param size 需要表示的位数
     */
    public BitMap(int size) {
        this.size = size;
        // 计算需要多少字节：size/8 向上取整
        this.bits = new byte[(size + 7) / 8];
    }
    
    /**
     * 设置某一位为1
     * @param position 位的位置（0-based）
     */
    public void set(int position) {
        if (position >= size || position < 0) {
            throw new IndexOutOfBoundsException("位置越界");
        }
        
        // 1. 计算在哪个字节
        int byteIndex = position / 8;
        
        // 2. 计算在字节中的哪一位
        int bitIndex = position % 8;
        
        // 3. 设置该位为1
        // 1 << bitIndex：将1左移bitIndex位
        // 例如：bitIndex=3 → 1<<3 = 00001000
        // |= ：按位或操作
        bits[byteIndex] |= (1 << bitIndex);
    }
    
    /**
     * 获取某一位的值
     * @param position 位的位置
     * @return 该位是否为1
     */
    public boolean get(int position) {
        if (position >= size || position < 0) {
            throw new IndexOutOfBoundsException("位置越界");
        }
        
        int byteIndex = position / 8;
        int bitIndex = position % 8;
        
        // 检查该位是否为1
        // bits[byteIndex] & (1 << bitIndex) 不为0表示该位为1
        return (bits[byteIndex] & (1 << bitIndex)) != 0;
    }
    
    /**
     * 清除某一位（设为0）
     * @param position 位的位置
     */
    public void clear(int position) {
        int byteIndex = position / 8;
        int bitIndex = position % 8;
        
        // 将指定位设为0，其他位不变
        // ~(1 << bitIndex)：取反，例如 00001000 → 11110111
        bits[byteIndex] &= ~(1 << bitIndex);
    }
    
    /**
     * 统计1的个数（popcount）
     */
    public int count() {
        int count = 0;
        for (byte b : bits) {
            // Java 8+ 可以使用 Integer.bitCount
            count += Integer.bitCount(b & 0xFF);
        }
        return count;
    }
    
    /**
     * 获取位图大小
     */
    public int size() {
        return size;
    }
    
    /**
     * 获取占用的字节数
     */
    public int byteSize() {
        return bits.length;
    }
}