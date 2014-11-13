/*
 * 
 */
package at.fhhgb.mc.wasserapp.rssfeed;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

// TODO: Auto-generated Javadoc
/**
 * Illustrative, as in: nowhere near complete or ready for production use ;).
 */
public class FeedRss implements Parcelable {

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<FeedRss> CREATOR = new Parcelable.Creator<FeedRss>(){
        @Override
        public FeedRss createFromParcel(Parcel source) {
            return new FeedRss(source);
        }

        @Override
        public FeedRss[] newArray(int size) {
            return new FeedRss[size];
        }
    };

    /**
     * The Class Item.
     */
    public static class Item {
        
        /** The url. */
        public String url;
        
        /** The title. */
        public String title;
        
        /**
         * Instantiates a new item.
         *
         * @param url the url
         * @param title the title
         */
        public Item(String url, String title) {
            this.url = url;
            this.title = title;
        }
    }

    /** The items. */
    private List<Item> items;

    /**
     * Instantiates a new feed rss.
     *
     * @param parcel the parcel
     */
    public FeedRss(Parcel parcel) {
        items = new ArrayList<Item>();
        int len = parcel.readInt();
        for (int i=0; i<len; i++)
            items.add(new Item(
                    parcel.readString(),
                    parcel.readString()));
    }

    /**
     * Instantiates a new feed rss.
     */
    public FeedRss(){
        items = new ArrayList<Item>();
    }

    /**
     * Adds the.
     *
     * @param url the url
     * @param title the title
     */
    public void add(String url, String title) {
        items.add(new Item(url, title));
    }

    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return items.size();
    }

    /**
     * Gets the.
     *
     * @param i the i
     * @return the item
     */
    public Item get(int i){
        return items.get(i);
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /* (non-Javadoc)
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(items.size());
        for (Item i : items) {
            dest.writeString(i.url);
            dest.writeString(i.title);
        }
    }
}
