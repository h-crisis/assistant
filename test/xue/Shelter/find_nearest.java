package org.geotools.Shelter;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.graph.build.feature.FeatureGraphGenerator;
import org.geotools.graph.build.line.LineStringGraphGenerator;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.basic.BasicNode;
import org.geotools.graph.traverse.standard.DijkstraIterator;
import org.geotools.styling.StyleFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;

import java.awt.*;
import java.awt.List;
import java.awt.Point;
import java.io.*;
import java.util.*;

/**
 * Created by jiao.xue on 2017/02/08.
 */
public class find_nearest {


    private static StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
    private static String geometryAttributeName;

    public enum GeomType {POINT, LINE, POLYGON}


    private static final Color LINE_COLOUR = Color.RED;
    private static final Color FILL_COLOUR = Color.RED;
    private static final Color destinationfillColor = Color.GREEN;
    private static final Color destinationoutlineColor = Color.GREEN;
    private static final float OPACITY = 1.0f;
    private static final float LINE_WIDTH = 5.0f;
    private static final float POINT_SIZE = 3.0f;

    private static Graph graph;
    public static LineStringGraphGenerator lineStringGen;
    private static Node nearestNode = null;
    private static Double distanceFromGraphNode = 0.0;

    private static File file;

    private static File inFile1;
    private static File inFile2;
    private static File Shape;

    find_nearest(File Shape, File inFile1, File inFile2, File out) throws Exception {
        build(Shape); // build graph
        //File out = new File("/Users/jiao/Desktop/shortestpath_test/output.csv");
        //if(!out.exists()) {
        //  out.createNewFile();
        //}
        find_nearest(inFile1, inFile2, out);

    }


    private static Graph buildGraph(FeatureCollection fc) throws IOException {

        lineStringGen = new LineStringGraphGenerator();
        FeatureGraphGenerator featureGen = new FeatureGraphGenerator(
                lineStringGen);
        FeatureIterator iter = fc.features();

        while (iter.hasNext()) {
            featureGen.add(iter.next());
        }

        iter.close();

        return featureGen.getGraph();
    }

    //Build a graph network from the routable shapefile for further routing services shpからgraphの変換
    public static void build(File file) throws Exception {
        // file = JFileDataStoreChooser.showOpenFile("shp", null);//shpを読み込む

        FileDataStore filestore = FileDataStoreFinder.getDataStore(file);//shpのデータを取り出す
        SimpleFeatureSource featureSource = filestore.getFeatureSource();//access all the features using a single method call:

        graph = buildGraph(featureSource.getFeatures());

        Collection nodes = graph.getNodes();//ノードの
        Collection edges = graph.getEdges();//辺のセット

        //検証
        Iterator iterator = edges.iterator();
        while (iterator.hasNext()) {//全ての辺の端点をとる
            Edge edge = (Edge) iterator.next();
            // Node node_test = (Node) iterator.next();//
            // Point p = (Point) node_test.getObject();//
            com.vividsolutions.jts.geom.Point start = (com.vividsolutions.jts.geom.Point) edge.getNodeA().getObject();
            com.vividsolutions.jts.geom.Point end = (com.vividsolutions.jts.geom.Point) edge.getNodeB().getObject();
            //System.out.println(start.getX() + " " + start.getY() + "to"
            //		+ end.getX() + " " + end.getY());

        }
        filestore.dispose();

    }

    //Rescale the path as the a list of points with same interval
    public static Path rescalePath(Path p, double interval) {
        Path newpath = new Path();
        int counter = 0;
        double dist = 0;//dist between two nodes
        double numerator = 0;
        double denominator = 0;
        double ratio = 0;
        double tempdist = 0;
        Node previous = null,current = null;
        GeometryFactory gf = new GeometryFactory();
        com.vividsolutions.jts.geom.Point point;


        double dist_prev_point = 0; //distance between previous and point p
        double dist_prev_cur = 0; //distance between previous and current
        double dist_cur_point = 0;
        double x =0, y = 0;
        if(p!=null){
            //System.out.println(p.size());
            current = (Node)p.get(0);
            point = gf.createPoint(new Coordinate(((com.vividsolutions.jts.geom.Point) current
                    .getObject()).getY(), ((com.vividsolutions.jts.geom.Point) current.getObject()).getX())); //Reverse the X axis and Y axis for map display purposes

            //include the start point
            newpath.add(counter++,point);

            previous = current;

            for(int i = 1; i<p.size();i++){

                previous = current;
                current = (Node)p.get(i);

                if(current != null){

                    //after move update the distance
                    dist_prev_point = CalculateEuclideanDistance(((com.vividsolutions.jts.geom.Point)previous.getObject()).getY(),((com.vividsolutions.jts.geom.Point)previous.getObject()).getX(), point.getX(),point.getY());
                    dist_prev_cur = CalculateEuclideanDistance(((com.vividsolutions.jts.geom.Point)previous.getObject()).getY(),((com.vividsolutions.jts.geom.Point)previous.getObject()).getX(), ((com.vividsolutions.jts.geom.Point)current.getObject()).getY(),((com.vividsolutions.jts.geom.Point)current.getObject()).getX());
                    dist_cur_point = CalculateEuclideanDistance(((com.vividsolutions.jts.geom.Point)current.getObject()).getY(),((com.vividsolutions.jts.geom.Point)current.getObject()).getX(), point.getX(),point.getY());

                    if(dist_prev_cur < dist_cur_point && dist_prev_point < dist_cur_point){ // Position: point -> previous -> current

                        dist = CalculateEuclideanDistance(point.getX(),point.getY(), ((com.vividsolutions.jts.geom.Point)previous.getObject()).getY(),((com.vividsolutions.jts.geom.Point)previous.getObject()).getX());
                        numerator = interval;
                        denominator = dist;
                        ratio = numerator/denominator;

                        x = point.getX() + ratio*(((com.vividsolutions.jts.geom.Point)previous.getObject()).getY()-point.getX());
                        y = point.getY() + ratio*(((com.vividsolutions.jts.geom.Point)previous.getObject()).getX()-point.getY());
                        point = gf.createPoint(new Coordinate(x,y));
                        newpath.add(counter++,point);

                    }
                    else if(dist_prev_cur < dist_prev_point && dist_prev_point > dist_cur_point) // Position: previous -> current -> point
                    {
                        i++;
                    }
                    else{ // Position: all the others
                        dist = CalculateEuclideanDistance(point.getX(),point.getY(), ((com.vividsolutions.jts.geom.Point)current.getObject()).getY(),((com.vividsolutions.jts.geom.Point)current.getObject()).getX());
                        numerator = interval;
                        denominator = dist;
                        ratio = numerator/denominator;

                        x = point.getX() + ratio*(((com.vividsolutions.jts.geom.Point)current.getObject()).getY()-point.getX());
                        y = point.getY() + ratio*(((com.vividsolutions.jts.geom.Point)current.getObject()).getX()-point.getY());
                        point = gf.createPoint(new Coordinate(x,y));
                        newpath.add(counter++,point);
                    }

                }



                //include the destination point
                if(i == p.size()-1){

                    com.vividsolutions.jts.geom.Point finalpoint = gf.createPoint(new Coordinate(((com.vividsolutions.jts.geom.Point) current
                            .getObject()).getY(), ((com.vividsolutions.jts.geom.Point) current.getObject()).getX()));
                    while ((dist = CalculateEuclideanDistance(point.getX(),point.getY(), finalpoint.getX(),finalpoint.getY())) > interval)
                    {
                        numerator = interval;
                        denominator = dist;
                        ratio = numerator/denominator;

                        x = point.getX() + ratio*(finalpoint.getX()-point.getX());
                        y = point.getY() + ratio*(finalpoint.getY()-point.getY());
                        point = gf.createPoint(new Coordinate(x,y));
                        newpath.add(counter++,point);
                    }
                    newpath.add(counter,finalpoint);
                }

            }
        }
        else
            System.out.println("There is no such route");
        return newpath;

    }

    public static Path dijkstraShortestPath(Node from, Node to) {//ノードの間のpathを

        DijkstraShortestPathFinder pf;
        DijkstraIterator.EdgeWeighter weighter = new DijkstraIterator.EdgeWeighter() {
            public double getWeight(Edge e) {
                SimpleFeature aLineString = (SimpleFeature) e.getObject();
                Geometry geom = (Geometry) aLineString.getDefaultGeometry();
                return geom.getLength();
            }
        };

        pf = new DijkstraShortestPathFinder(graph, from, weighter);
        pf.calculate();
        // double cost;
        //cost=pf.getCost(to);
        //System.out.print(cost);
        //System.out.print('\n');
        return pf.getPath(to);
    }
    /////////////pathの長さを求める
    //////////

    public static double dijkstraShortestPathCost(Node from, Node to) {//ノードの間のpathを

        DijkstraShortestPathFinder pf;
        DijkstraIterator.EdgeWeighter weighter = new DijkstraIterator.EdgeWeighter() {
            public double getWeight(Edge e) {
                SimpleFeature aLineString = (SimpleFeature) e.getObject();
                Geometry geom = (Geometry) aLineString.getDefaultGeometry();
                return geom.getLength();
            }
        };

        pf = new DijkstraShortestPathFinder(graph, from, weighter);
        pf.calculate();
        //cost
        double cost;
        cost = pf.getCost(to);

        return cost;
    }


    // Get nearest graph node

    public static Node getNearestGraphNode(LineStringGraphGenerator lsgg,
                                           Graph g, com.vividsolutions.jts.geom.Point pointy) {//pointyはinterest point
        // initiliase the distance
        Node nearestNode = null;
        Double distanceFromGraphNode;
        double dist = 999999999;

        // loops through the nodes of the graph and finds the node closest to
        // the point of interest
        for (Object o : g.getNodes()) {
            Node n = (Node) o;
            com.vividsolutions.jts.geom.Point p = ((com.vividsolutions.jts.geom.Point) n.getObject());
            double newdist = CalculateEuclideanDistance(pointy.getX(),
                    pointy.getY(), p.getCoordinate().x, p.getCoordinate().y);

            if (newdist < dist) {
                dist = newdist;
                nearestNode = n;
            }
        }

        // returns the node closest to the locaiton of interest
        Node source = (BasicNode) lsgg.getNode(new Coordinate(
                ((com.vividsolutions.jts.geom.Point) nearestNode.getObject()).getCoordinate().x,
                ((com.vividsolutions.jts.geom.Point) nearestNode.getObject()).getCoordinate().y));

        // stores the distance between node and location of interest, can be
        // accessed by calling the getDistanceFromGraphNode() function
        ///////////
        distanceFromGraphNode = dist;




        return nearestNode;
    }


    private static double CalculateEuclideanDistance(double xOrig,
                                                     double yOrig, double xDest, double yDest) {
        // calculates traight lne distance
        double distance = Math.sqrt((xDest - xOrig) * (xDest - xOrig)
                + (yDest - yOrig) * (yDest - yOrig));
        return distance;

    }

    public static LinkedList<String> search_origin_path(double start_lat, double start_lon, double end_lat, double end_lon, File shape) throws Exception {
        //
        build(shape);
        LinkedList<String> display_path = new LinkedList<String>();
        LinkedList<String> path = new LinkedList<>();
        // set start point and end point
        GeometryFactory gf = new GeometryFactory();
        com.vividsolutions.jts.geom.Point startPoint = gf
                .createPoint(new Coordinate(start_lon,start_lat));

        com.vividsolutions.jts.geom.Point endPoint = gf.createPoint(new Coordinate(end_lon,end_lat));

        Node start = getNearestGraphNode(lineStringGen, graph, startPoint);
        Node end = getNearestGraphNode(lineStringGen, graph, endPoint);
        // Find path
        Path p = dijkstraShortestPath(start, end);
        System.out.println(p+"\n");



        Node previous = null, node = null, current = null;
        com.vividsolutions.jts.geom.Point point;
        int i= 0;

        if(p!=null){
            current=(Node)p.get(0);//先頭の処理
            point = gf.createPoint(new Coordinate(((com.vividsolutions.jts.geom.Point) current.getObject()).getY(), ((com.vividsolutions.jts.geom.Point) current.getObject()).getX()));
            path.add(i++, point.getY() + ";" + point.getX());

            for(i=1;i<p.size();i++){
                previous=current;
                current=(Node)p.get(i);
                if(current!=null){
                    point = gf.createPoint(new Coordinate(((com.vividsolutions.jts.geom.Point) current.getObject()).getY(), ((com.vividsolutions.jts.geom.Point) current.getObject()).getX()));
                    path.add(i, point.getY() + ";" + point.getX());

                }
            }


        }


        return path;

    }

    public static LinkedList<String> search_path(double start_lat, double start_lon, double end_lat, double end_lon, File shape, double path_unit) throws Exception {
        //
        build(shape);
        LinkedList<String> path = new LinkedList<String>();
        // set start point and end point
        GeometryFactory gf = new GeometryFactory();
        com.vividsolutions.jts.geom.Point startPoint = gf
                .createPoint(new Coordinate(start_lon,start_lat));

        com.vividsolutions.jts.geom.Point endPoint = gf.createPoint(new Coordinate(end_lon,end_lat));

        Node start = getNearestGraphNode(lineStringGen, graph, startPoint);
        Node end = getNearestGraphNode(lineStringGen, graph, endPoint);
        // Find path
        Path p = dijkstraShortestPath(start, end);
        Path p_average = rescalePath(p, path_unit);

        Node previous = null, node = null;
        Iterator iterator = p_average.riterator();
        int i = 0;
        com.vividsolutions.jts.geom.Point point;
        while (iterator.hasNext()) {

            point = (com.vividsolutions.jts.geom.Point) iterator.next();
            com.vividsolutions.jts.geom.Point displaypoint = gf.createPoint(new Coordinate(point.getX(), point.getY()));


            path.add(i, displaypoint.getY() + ";" + displaypoint.getX());

        }

        return path;

    }
    //////////////////////////////////
    public static double search_distance(double start_lat, double start_lon, double end_lat, double end_lon, File shape) throws Exception {
        build(shape);
        // set start point and end point
        GeometryFactory gf = new GeometryFactory();
        com.vividsolutions.jts.geom.Point startPoint = gf
                .createPoint(new Coordinate(start_lon,start_lat));

        com.vividsolutions.jts.geom.Point endPoint = gf.createPoint(new Coordinate( end_lon,end_lat));

        Node start = getNearestGraphNode(lineStringGen, graph, startPoint);
        Node end = getNearestGraphNode(lineStringGen, graph, endPoint);

        double pathcosts = dijkstraShortestPathCost(start, end);//ノードの最短距離
        //System.out.println(pathcosts+"\n");


        return pathcosts;

    }
    //////////////////////////////////


    //public static void main(String[] args) throws Exception {
    public static void find_nearest(File inFile1, File inFile2, File out){

        //  build(); // build graph

//shelterとhospitalの経路を探す
        // File shelter = new File("/Users/jiao/Desktop/shortestpath_test/shelter_latitude_longtitude_test.csv");
        // File hospital = new File("/Users/jiao/Desktop/shortestpath_test/hospital_latitude_longtitude_test.csv");
        try {
            BufferedReader shelter_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "SHIFT_JIS"));

            //File file = new File("/Users/jiao/Desktop/shortestpath_test/output.csv");
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "SHIFT_JIS"));


            String line1, line2;
            double lon1, la1, lon2, la2;
            boolean midashi1 = true;
            boolean midashi2 = true;
            Node starts;
            Node ends;
            com.vividsolutions.jts.geom.Point startPoints;
            com.vividsolutions.jts.geom.Point endPoints;
            Double pathcosts=9999.0;
            GeometryFactory gfs = new GeometryFactory();
            while ((line1 = shelter_info.readLine()) != null) {

                String pair1[] = line1.split(",");
                BufferedReader hospital_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile2), "SHIFT_JIS"));
                while ((line2 = hospital_info.readLine()) != null) {
                    if (midashi1 == true && midashi2 == true) {
                        String pair2[] = line2.split(",");
                        output.write(pair1[1] + "," + pair2[1] + "," + "distance");
                        midashi1 = false;
                        break;
                    } else {
                        if (midashi2 == true) {
                            String pair2[] = line2.split(",");

                            lon1 = Double.parseDouble(pair1[2]);
                            la1 = Double.parseDouble(pair1[3]);
                            lon2 = Double.parseDouble(pair2[2]);
                            la2 = Double.parseDouble(pair2[3]);

                            // GeometryFactory gf = new GeometryFactory();
                            startPoints = gfs
                                    .createPoint(new Coordinate(lon1, la1));

                            endPoints = gfs.createPoint(new Coordinate(lon2, la2));

                            starts = getNearestGraphNode(lineStringGen, graph, startPoints);
                            ends = getNearestGraphNode(lineStringGen, graph, endPoints);
                            if(starts!=null && ends!=null) {//

                                pathcosts = dijkstraShortestPathCost(starts, ends);//ノードの最短距離
                            }//
                            output.write("\n" + pair1[1] + "," + pair2[1] + "," + pathcosts);
                        } else {
                            String pair2[] = line2.split(",");
                            midashi2 = true;
                        }

                    }
                }
                hospital_info.close();
                midashi2 = false;

            }
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void find_top(File inFile1, File out) {
        try {
            BufferedReader distance_info = new BufferedReader(new InputStreamReader(new FileInputStream(inFile1), "SHIFT_JIS"));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, false), "SHIFT_JIS"));

            String line1 = distance_info.readLine(); // 1行目は見出し
            output.write(line1);//表の第一行目コピー
            int count=0;
            String data1="dame";

            Map<String,Double> map = new HashMap<String, Double>();// 病院がキー、距離が値


            while((line1 = distance_info.readLine()) != null) {
                String pair2[] = line1.split(",");
                if(data1.equals(pair2[0])){
                    map.put(pair2[1], Double.parseDouble(pair2[2]));
                }
                else{
                    java.util.List<Map.Entry<String, Double>> list =
                            new ArrayList<Map.Entry<String, Double>>(map.entrySet());
                    Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
                        //sorting
                        public int compare(Map.Entry<String, Double> o1,
                                           Map.Entry<String, Double> o2) {
                            return o1.getValue().compareTo(o2.getValue());
                        }

                    });
                    for(Map.Entry<String,Double> mapping:list){
                        if(count<1) {
                            output.write("\n" + data1 + "," + mapping.getKey() + "," + mapping.getValue());//
                            count++;
                        }
                        // System.out.println(mapping.getKey()+":"+mapping.getValue());
                    }
                    count=0;
                    map.clear();
                    data1 = pair2[0];
                    map.put(pair2[1], Double.parseDouble(pair2[2]));

                }
            }
            // 循環終わり
            //最後の一行の処理
            java.util.List<Map.Entry<String, Double>> list =
                    new ArrayList<Map.Entry<String, Double>>(map.entrySet());
            Collections.sort(list,new Comparator<Map.Entry<String,Double>>() {
                //sorting
                public int compare(Map.Entry<String, Double> o1,
                                   Map.Entry<String, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }

            });
            for(Map.Entry<String,Double> mapping:list){
                if(count<1) {
                    output.write("\n" + data1 + "," + mapping.getKey() + "," + mapping.getValue());//
                    count++;
                }
            }
            map.clear();
            output.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
