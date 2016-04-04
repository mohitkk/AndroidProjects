package mkumar27.msse.asu.edu.layoutsandbasicuicontrols;

/**
 * Copyright (c) 2015 Tim Lindquist,
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Purpose: This class is part of an example developed for the mobile
 * computing class at ASU Poly. The application provides a waypoint service.
 * The client and service are both written in Java and they 
 * communicate using JSON-RPC.
 * <p/>
 * This is the interface to the waypoint service
 *
 * @author Tim Lindquist
 * @version 2/8/2015
 **/
public interface WaypointServer {
   public String serviceInfo();
   public boolean saveToFile();
   public boolean add(Waypoint wp);
   public boolean remove(String wpName);
   public Waypoint get(String wpName);
   public String[] getNames();
   public String[] getCategoryNames();
   public String[] getNamesInCategory(String aCat);
   public double distanceGCTo(String from, String to);
   public double bearingGCInitTo(String from, String to);
}
