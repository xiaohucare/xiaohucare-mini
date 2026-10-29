package com.xiaohucare.common.extra.tool.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
	private static final int  BUFFER_SIZE = 2 * 1024;
    
	private ZipUtils(){
    }
    
    public static void doCompress(String srcFile, String zipFile) throws IOException {
        doCompress(new File(srcFile), new File(zipFile));
    }
    
    /**
     * 文件压缩
     * @param srcFile 目录或者单个文件
     * @param zipFile 压缩后的ZIP文件
     */
    public static void doCompress(File srcFile, File zipFile) throws IOException {
    	if(!zipFile.getParentFile().exists()) {
    		zipFile.getParentFile().mkdirs();
    	}
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            doCompress(srcFile, out);
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();//记得关闭资源
        }
    }
    
    public static void doCompress(String filelName, ZipOutputStream out) throws IOException{
        doCompress(new File(filelName), out);
    }
    
    public static void doCompress(File file, ZipOutputStream out) throws IOException{
        doCompress(file, out, "");
    }
    
    public static void doCompress(File inFile, ZipOutputStream out, String dir) throws IOException {
        if ( inFile.isDirectory() ) {
            File[] files = inFile.listFiles();
            if (files!=null && files.length>0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    ZipUtils.doCompress(file, out, name);
                }
            }
        } else {
             ZipUtils.doZip(inFile, out, dir);
        }
    }
    
    public static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName = null;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);
        
        int len = 0 ;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.closeEntry();
        fis.close();
    }
    
    /**
         * 压缩成ZIP 方法1
         * @param srcDir 压缩文件夹路径 
         * @param out    压缩文件输出流
         * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构; 
         *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
         * @throws RuntimeException 压缩失败会抛出运行时异常
         */
       public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
                throws RuntimeException{
            long start = System.currentTimeMillis();
            ZipOutputStream zos = null ;
            try {
                zos = new ZipOutputStream(out);
                File sourceFile = new File(srcDir);
                compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
                long end = System.currentTimeMillis();
                System.out.println("压缩完成，耗时：" + (end - start) +" ms");
            } catch (Exception e) {
                throw new RuntimeException("zip error from ZipUtils",e);
            }finally{
                if(zos != null){
                    try {
                        zos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
       /**
            * 压缩成ZIP 方法2
            * @param srcFiles 需要压缩的文件列表
            * @param out           压缩文件输出流
            * @throws RuntimeException 压缩失败会抛出运行时异常
            */
           public static void toZip(List<File> srcFiles , OutputStream out)throws RuntimeException {
               long start = System.currentTimeMillis();
               ZipOutputStream zos = null ;
               try {
                   zos = new ZipOutputStream(out);
                   for (File srcFile : srcFiles) {
                       byte[] buf = new byte[BUFFER_SIZE];
                       zos.putNextEntry(new ZipEntry(srcFile.getName()));
                       int len;
                       FileInputStream in = new FileInputStream(srcFile);
                       while ((len = in.read(buf)) != -1){
                           zos.write(buf, 0, len);
                       }
                       zos.closeEntry();
                       in.close();
                   }
                   long end = System.currentTimeMillis();
                   System.out.println("压缩完成，耗时：" + (end - start) +" ms");
               } catch (Exception e) {
                   throw new RuntimeException("zip error from ZipUtils",e);
               }finally{
                   if(zos != null){
                       try {
                           zos.close();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }
     /* 
     * 递归压缩方法
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构; 
     *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
            boolean KeepDirStructure) throws Exception{
        byte[] buf = new byte[BUFFER_SIZE];
        if(sourceFile.isFile()){
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if(listFiles == null || listFiles.length == 0){
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if(KeepDirStructure){
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            }else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(),KeepDirStructure);
                    }
                    
                }
            }
        }
    }
    
}
  