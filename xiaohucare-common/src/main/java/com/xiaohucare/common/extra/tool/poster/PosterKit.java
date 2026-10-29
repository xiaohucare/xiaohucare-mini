package com.xiaohucare.common.extra.tool.poster;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.util.DigestUtils;

import com.xiaohucare.common.extra.tool.poster.util.ColorTools;
import com.xiaohucare.common.extra.tool.poster.util.Drawable;
import com.xiaohucare.common.extra.tool.poster.util.PosterUtils;
import com.xiaohucare.common.extra.tool.utils.DateUtils;
import com.xiaohucare.common.extra.utils.JsonUtil;
import com.xiaohucare.common.utils.file.FileUtils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

public class PosterKit {
    enum Format {
        png, jpg
    }
    public static void drawPath(PosterVo posterVo,String pathAndFileName) throws IOException {
        BufferedImage image=drawBufferedImage(posterVo);
        File file=new File(pathAndFileName);
        ImageIO.write(image, posterVo.getFormat().toString(), file); // 把文件写入图片
    }
    public static void draw(PosterVo posterVo,File pathAndFileName) throws IOException {
        BufferedImage image=drawBufferedImage(posterVo);
        ImageIO.write(image, posterVo.getFormat().toString(), pathAndFileName); // 把文件写入图片
    }
    public static BufferedImage drawBufferedImage(PosterVo posterVo) throws IOException {
        Integer width=posterVo.getWidth();
        Integer height=posterVo.getHeight();
       // 初始化图片
       BufferedImage image = new BufferedImage(width, height,posterVo.getFormat().equals(Format.png) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_3BYTE_BGR);
       // create graphics
       Graphics2D gd = image.createGraphics();
       // 初始化画布层级 map
       Map<Integer, ArrayList<Drawable>> indexMap = new HashMap<>();
       ArrayList<Drawable> drawables;
       // 如果有背景，画个矩形做背景
       if (posterVo.getBackgroundColor()!= null) {
           gd.setColor(ColorTools.String2Color(posterVo.getBackgroundColor()));
           gd.fillRect(0, 0, width, height);
       }
       if (posterVo.getBlocks()!= null) {
           // 遍历 blocks
           for (Block block : posterVo.getBlocks()) {
               if(block.getIndex()<0) {
               }else {
                   push2map(indexMap, block);
               }
           }
       }
       if (posterVo.getLines()!= null) {
           // 遍历 lines
           for (Line line : posterVo.getLines()) {
               if(line.getIndex()<0) {
               }else {
                   push2map(indexMap, line);
               }
           }
       }
       if (posterVo.getImages() != null) {
           // 遍历 images
           for (Image img : posterVo.getImages()) {
               if(img.getIndex()<0) {
               }else {
                   push2map(indexMap, img);
               }
           }
       }
       if (posterVo.getTexts() != null) {
           // 遍历 texts
           for (Text text : posterVo.getTexts()) {
               if(text.getIndex()<0) {
               }else {
                   push2map(indexMap, text);
               }
           }
       }
       // 按 index 顺序执行绘画过程
       for (Integer index : indexMap.keySet()) {
           drawables = indexMap.get(index);
           if (drawables != null) {
               for (Drawable drawable : drawables) {
                   drawable.draw(gd, width, height);
               }
           }
       }
       gd.dispose();
       return image;
   }
    public  String key() {
        return DigestUtils.md5DigestAsHex(this.toString().getBytes());
    }
    public  String toStringtoString() {
        return this.toString();
    }
    private static void push2map(Map<Integer, ArrayList<Drawable>> indexMap, Drawable drawable) {
        ArrayList<Drawable> drawables = indexMap.get(drawable.getZIndex());
        drawables = drawables == null ? new ArrayList<>() : drawables;
        drawables.add(drawable);
        indexMap.put(drawable.getZIndex(), drawables);
    }
}
