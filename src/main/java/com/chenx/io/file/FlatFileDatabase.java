package com.chenx.io.file;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 一个基于RandomAccessFile的平面文件数据库
 */
public class FlatFileDatabase {
    public static final int PNUMLEN = 20;   // partNum长度，单位字符
    public static final int DESCLEN = 30;   // partDesc长度，单位字符
    public static final int QUANLEN = 4;    // qty长度，单位字节
    public static final int COSTLEN = 4;    // ucost长度，单位字节

    // record总长度，因为每个字符占用2字节，所以*2
    public static final int RECLEN = 2 * PNUMLEN + 2 * DESCLEN + QUANLEN + COSTLEN;

    private RandomAccessFile raf;

    public FlatFileDatabase(String path) throws IOException {
        raf = new RandomAccessFile(path, "rw");
    }

    /**
     * 添加记录
     */
    public void append(String partNum, String partDesc, int qty, int ucost) throws IOException {
        raf.seek(raf.length()); // 移动到文件末尾
        write(partNum, partDesc, qty, ucost);
    }

    /**
     * 对于close方法，因为产生异常的情况很罕见，内部处理掉异常，仅在异常产生时记录日志，省略掉使用时的异常处理
     */
    public void close() {
        try {
            raf.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public int numRecs() throws IOException {
        return (int) raf.length() / RECLEN;
    }

    /**
     * 选取指定recno的记录
     */
    public Part select(int recno) throws IOException {
        if (recno < 0 || recno >= numRecs()) {
            throw new IllegalArgumentException(recno + " out of range.");
        }
        raf.seek(recno * RECLEN);
        return read();
    }

    public void update(int recno, String partNum, String partDesc, int qty, int ucost) throws IOException {
        if (recno < 0 || recno > numRecs()) {
            throw new IllegalArgumentException(recno + " out of range.");
        }
        raf.seek(recno * RECLEN);
        write(partNum, partDesc, qty, ucost);
    }

    private Part read() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PNUMLEN; i++) {
            sb.append(raf.readChar());
        }
        String partNum = sb.toString().trim();
        sb.setLength(0);
        for (int i = 0; i < DESCLEN; i++) {
            sb.append(raf.readChar());
        }
        String partDesc = sb.toString().trim();
        int qty = raf.readInt();
        int ucost = raf.readInt();

        return new Part(partNum, partDesc, qty, ucost);
    }

    /**
     * 私有方法，仅供append方法调用
     */
    private void write(String partNum, String partDesc, int qty, int ucost) throws IOException {
        StringBuilder sb = new StringBuilder(partNum);
        // 如果partNum超出长度，通过setLength方法截断
        if (sb.length() > PNUMLEN) {
            sb.setLength(PNUMLEN);
        } else if (sb.length() < PNUMLEN) {
            // 如果实际内容的长度不足，通过“ ”填充以维护统一的格式
            int len = PNUMLEN - sb.length();
            for (int i = 0; i < len; i++) {
                sb.append(" ");
            }
        }
        raf.writeChars(sb.toString());

        sb = new StringBuilder(partDesc);
        if (sb.length() > DESCLEN) {
            sb.setLength(DESCLEN);
        } else if (sb.length() < DESCLEN) {
            int len = DESCLEN - sb.length();
            for (int i = 0; i < len; i++) {
                sb.append(" ");
            }
        }
        raf.writeChars(sb.toString());
        raf.writeInt(qty);
        raf.writeInt(ucost);
    }

    public static class Part {
        private String partNum;
        private String desc;
        private int qty;
        private int ucost;

        public Part(String partNum, String desc, int qty, int ucost) {
            this.partNum = partNum;
            this.desc = desc;
            this.qty = qty;
            this.ucost = ucost;
        }

        public String getPartNum() {
            return partNum;
        }

        public String getDesc() {
            return desc;
        }

        public int getQty() {
            return qty;
        }

        public int getUcost() {
            return ucost;
        }
    }

    /**
     * 测试FlatFileDatabase
     */
    public static void main(String[] args) throws Exception {
        FlatFileDatabase db = null;
        try {
            db = new FlatFileDatabase("./demo");
//            if (db.numRecs() == 0) {
                db.append("1-9009-2234-4x", "Wiper blade micro edge", 30, 2468);
                db.append("1-3233-4423-7j", "parking what this", 5, 1439);
                db.append("2-3399-6693-2m", "i dont know this", 22, 813);
                db.append("2-59s9-2029-6k", "Turbo oil lines", 26, 155);
                db.append("3-1299-3299-9u", "中文汉字简体", 9, 20200);
//            }
            dumpRecords(db);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    private static void dumpRecords(FlatFileDatabase db) throws IOException {
        for (int i = 0; i < db.numRecs(); i++) {
            FlatFileDatabase.Part part = db.select(i);
            System.out.print(format(part.getPartNum(), FlatFileDatabase.PNUMLEN, true));
            System.out.print(" | ");
            System.out.print(format(part.getDesc(), FlatFileDatabase.DESCLEN, true));
            System.out.print(" | ");
            System.out.print(format("" + part.getQty(), 10, false));
            System.out.print(" | ");
            String s = part.getUcost() / 100 + "." + part.getUcost() %
                100;
            if (s.charAt(s.length() - 2) == '.') s += "0";
            System.out.println(format(s, 10, false));
        }
        System.out.println("Number of records = " + db.numRecs());
        System.out.println();
    }

    static String format(String value, int maxWidth, boolean leftAlign)
    {
        StringBuffer sb = new StringBuffer();
        int len = value.length();
        if (len > maxWidth)
        {
            len = maxWidth;
            value = value.substring(0, len);
        }
        if (leftAlign)
        {
            sb.append(value);
            for (int i = 0; i < maxWidth - len; i++)
                sb.append(" ");
        }
        else
        {
            for (int i = 0; i < maxWidth - len; i++)
                sb.append(" ");
            sb.append(value);
        }
        return sb.toString();
    }
}

