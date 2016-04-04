package mkumar27.bsse.asu.edu.lab_6;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
/**
 * Copyright 2015 Mohit Kumar,
 * <p/>
 * TA and Instructor can download and execute this for evaluation purpose.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: activity to display information about a student
 *
 * @author Mohit Kumar mkumar27@asu.edu
 *         Software Engineering, Arizona State University Polytechnic
 * @version February 21, 2015
 */
public class ExpandableWaypointAdapter extends BaseExpandableListAdapter implements View.OnTouchListener,ExpandableListView.OnGroupExpandListener,ExpandableListView.OnGroupCollapseListener {
    private TextView currentSelectedTV = null;
    private MainActivity parent;
    //linked hash map ensures consistent order for iteration and toarray.
    private LinkedHashMap<String,String[]> model;
    public ExpandableWaypointAdapter(MainActivity parent) {
        android.util.Log.d(this.getClass().getSimpleName(),"in constructor so creating new model");
        this.model = new LinkedHashMap<String, String[]>();
        this.parent = parent;
        parent.elview.setOnGroupExpandListener(this);
        parent.elview.setOnGroupCollapseListener(this);
        setModelFromDB();
    }
    private void setModelFromDB(){
        try{
            model.clear();
            WayPointDb db = new WayPointDb((Context)parent);
            db.copyDB();
            SQLiteDatabase crsDB = db.openDB();
            crsDB.beginTransaction();
            Cursor cur = crsDB.rawQuery("select DISTINCT category from waypoint;",new String[]{});
            while (cur.moveToNext()){
                String category = cur.getString(0);
                Cursor waypts = crsDB.rawQuery(
                        "select name, waypointid from waypoint where waypoint.category=? ;",new String[]{category});
                ArrayList<String> wayArr = new ArrayList<String>();
                while(waypts.moveToNext()){
                    wayArr.add(waypts.getString(0)+" - "+waypts.getString(1));
                }
                model.put(category, wayArr.toArray(new String[]{}));
                android.util.Log.d(this.getClass().getSimpleName(),"adding to model: "+
                        category+" with "+ wayArr.toArray(new String[]{}).length+
                        " students.");
                waypts.close();
            }
            cur.close();
            crsDB.endTransaction();
            crsDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception creating adapter: "+
                    ex.getMessage());
        }
    }
    private void resetModel(LinkedHashMap<String,String[]> aModel){
        setModelFromDB();
        this.notifyDataSetChanged();
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String[] stuffTitles = model.keySet().toArray(new String[] {});
        return model.get(stuffTitles[groupPosition])[childPosition];
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            android.util.Log.d(this.getClass().getSimpleName(),"in getChildView null so creating new view");
            LayoutInflater inflater = (LayoutInflater) this.parent
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView txtListChild = (TextView)convertView.findViewById(R.id.lblListItem);
        convertView.setOnTouchListener(this);
        txtListChild.setText(childText);
        return convertView;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        String[] stuffTitles = model.keySet().toArray(new String[] {});
        //android.util.Log.d(this.getClass().getSimpleName(),"in getChildrenCount for: "+groupPosition+" returning: "+
        //                   model.get(stuffTitles[groupPosition]).length);
        return model.get(stuffTitles[groupPosition]).length;
    }
    @Override
    public Object getGroup(int groupPosition) {
        String[] stuffTitles = model.keySet().toArray(new String[] {});
        //android.util.Log.d(this.getClass().getSimpleName(),"in getGroup returning: "+stuffTitles[groupPosition]);
        return stuffTitles[groupPosition];
    }
    @Override
    public int getGroupCount() {
        String[] stuffTitles = model.keySet().toArray(new String[] {});
        //android.util.Log.d(this.getClass().getSimpleName(),"in getGroupCount returning: "+stuffTitles.length);
        return stuffTitles.length;
    }
    @Override
    public long getGroupId(int groupPosition) {
        //android.util.Log.d(this.getClass().getSimpleName(),"in getGroupPosition returning: "+groupPosition);
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        try {
            if (convertView == null) {
                android.util.Log.d(this.getClass().getSimpleName(), "in getGroupView null so creating new view");
                LayoutInflater inflater = (LayoutInflater) this.parent
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_group, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            //android.util.Log.d(this.getClass().getSimpleName(),"in getGroupView text is: "+lblListHeader.getText());
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
        }
        catch(Exception ex)
        {
            ex.getMessage();
        }
        //ImageView img = (ImageView)convertView.findViewById(R.id.group_image);
        //img.setImageResource(R.drawable.ic_book);
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        //android.util.Log.d(this.getClass().getSimpleName(),"in hasStableIds returning false");
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        String[] stuffTitles = model.keySet().toArray(new String[] {});
        //android.util.Log.d(this.getClass().getSimpleName(),"in isChildSelectable?  "+
        //                   model.get(stuffTitles[groupPosition])[childPosition]);
        return true;
    }
    public boolean onTouch(View v, MotionEvent event){
        // when the user touches a song, onTouch is called once for action down and again for action up
        // we only want to do something on one of those actions. event tells us which action.
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            android.util.Log.d(this.getClass().getSimpleName(),"in onTouch called for view of type: " +
                    v.getClass().toString());
            // onTouch is passed the textview's parent view, a linearlayout since thats what we set the
            // event on. Look at its children to find the textview
            if(v instanceof android.widget.LinearLayout){
                android.widget.LinearLayout layView = (android.widget.LinearLayout)v;
                for(int i=0; i<=layView.getChildCount(); i++){
                    if(layView.getChildAt(i) instanceof TextView){
                        // keep track of which stuff was most recently touched so it can be un-highlighted
                        if (currentSelectedTV != null){
                            currentSelectedTV.setBackgroundColor(parent.getResources().getColor(R.color.light_green));
                        }
                        TextView tmp = ((TextView)layView.getChildAt(i));
                        tmp.setBackgroundColor(Color.YELLOW);
                        //tmp.setSelected(true);
                        currentSelectedTV = tmp;
                        parent.selectedWaypoint=tmp.getText().toString();
                        //start new activity passing the selected waypoint string as an extension to the intent
                        android.util.Log.d(this.getClass().getSimpleName(),"TextView "+((TextView)layView.getChildAt(i)).getText()+" selected.");
                        Intent intent = new Intent(parent, WayPointDetail.class);
                        intent.putExtra("selected", parent.selectedWaypoint);
                        parent.startActivity(intent);
                        parent.finish();
                    }
                }
            }
            // code below never executes. onTouch is called on for textview's linearlayout parent
            if(v instanceof TextView){
                android.util.Log.d(this.getClass().getSimpleName(),"in onTouch called for: " +
                        ((TextView)v).getText());
            }
        }
        return true;
    }
    public void onGroupExpand(int groupPosition){
        android.util.Log.d(this.getClass().getSimpleName(),"in onGroupExpand called for: "+
                model.keySet().toArray(new String[] {})[groupPosition]);
        if (currentSelectedTV != null){
            currentSelectedTV.setBackgroundColor(parent.getResources().getColor(R.color.light_green));
            currentSelectedTV = null;
        }
        for (int i=0; i< this.getGroupCount(); i++) {
            if(i != groupPosition){
                parent.elview.collapseGroup(i);
            }
        }
    }
    public void onGroupCollapse(int groupPosition){
        android.util.Log.d(this.getClass().getSimpleName(),"in onGroupCollapse called for: "+
                model.keySet().toArray(new String[] {})[groupPosition]);
        if (currentSelectedTV != null){
            currentSelectedTV.setBackgroundColor(parent.getResources().getColor(R.color.light_green));
            currentSelectedTV = null;
        }
    }
}
