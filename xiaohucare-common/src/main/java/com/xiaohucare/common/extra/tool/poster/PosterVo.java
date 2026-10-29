package com.xiaohucare.common.extra.tool.poster;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.util.DigestUtils;

import com.xiaohucare.common.extra.tool.poster.util.Drawable;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PosterVo {
    enum Format {
        png, jpg
    }
    /**
     * 画布宽度
     */
    @NotNull(message = "画布宽度不能为空")
    private Integer width;

    /**
     * 画布高度
     */
    @NotNull(message = "画布高度不能为空")
    private Integer height;

    /**
     * 画布背景颜色
     */
    private String backgroundColor = null;

    /**
     * 图片格式,支持 png 和 jpg
     */
    private Format format = Format.png;

    /**
     * 文本列表
     */
    private ArrayList<Text> texts;

    /**
     * 图片列表
     */
    private ArrayList<Image> images;

    /**
     * 矩形列表
     */
    private ArrayList<Block> blocks;

    /**
     * 线列表
     */
    private ArrayList<Line> lines;

    private void push2map(Map<Integer, ArrayList<Drawable>> indexMap, Drawable drawable) {
        ArrayList<Drawable> drawables = indexMap.get(drawable.getZIndex());
        drawables = drawables == null ? new ArrayList<>() : drawables;
        drawables.add(drawable);
        indexMap.put(drawable.getZIndex(), drawables);
    }
    /**
     * 获取key
     *
     * @return String
     */
    public String key() {
        return DigestUtils.md5DigestAsHex(this.toString().getBytes());
    }
}
