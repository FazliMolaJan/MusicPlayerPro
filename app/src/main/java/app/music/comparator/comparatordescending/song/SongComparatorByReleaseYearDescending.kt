package app.music.comparator.comparatordescending.song

import app.music.model.BaseMusik
import java.util.*

class SongComparatorByReleaseYearDescending : Comparator<BaseMusik> {

    override fun compare(song1: BaseMusik, song2: BaseMusik): Int {
        //        String yearString1 = song1.getYear();
        //        String yearString2 = song2.getYear();
        //        Integer year1;
        //        Integer year2;
        //        if (null == yearString1) {
        //            year1 = 0;
        //        } else {
        //            year1 = Integer.parseInt(yearString1);
        //        }
        //        if (null == yearString2) {
        //            year2 = 0;
        //        } else {
        //            year2 = Integer.parseInt(yearString2);
        //        }

        return 0.compareTo(Integer.valueOf(song1.year).compareTo(Integer.valueOf(song2.year)))
    }
}
