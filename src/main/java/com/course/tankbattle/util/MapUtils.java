package com.course.tankbattle.util;

import com.course.tankbattle.entity.Iron;
import com.course.tankbattle.entity.Water;
import com.course.tankbattle.resource.map.Map;
import com.course.tankbattle.entity.Stuff;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

public final class MapUtils {
    private MapUtils() {
    }

    /**
     * 检查(x,y)是否被河流或铁块挡住，不可作为生成点
     */
    public static boolean isSpawnBlocked(int x, int y, Map map) {
        for (Water w : map.getWaters()) {
            if (Math.abs(x - w.getX()) < 30 && Math.abs(y - w.getY()) < 30) return true;
        }
        for (Iron i : map.getIrons()) {
            if (Math.abs(x - i.getX()) < 30 && Math.abs(y - i.getY()) < 30) return true;
        }
        return false;
    }

    public static Stuff getNearestStuff(int x, int y) {
        int gridX = ((x + 10) / 20) * 20 + 10;
        int gridY = ((y + 10) / 20) * 20 + 10;
        return new Stuff(gridX, gridY);
    }

    public static Collection<File> listCustomMaps() {
        File customFile = new File(System.getProperty("user.home") + File.separator + ".tankBattle" + File.separator + "custom");
        if (!customFile.exists()) {
            return Collections.emptyList();
        }
        return FileUtils.listFiles(customFile, FileFilterUtils.suffixFileFilter("xml"), DirectoryFileFilter.INSTANCE);
    }
}
