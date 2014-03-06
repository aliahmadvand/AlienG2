package com.ali.Mtree;

/*
 * node.java
 *
 * Created on September 26, 2000, 9:06 AM
 */

//package rates;
import java.lang.*;
import java.lang.reflect.*;
import java.util.*;
/**
 *
 * @author  noname
 * @version 
 */
public class node extends Object {

    /** Creates new node */
    public node() {
    }

    int seqlength=0;
    int bootstrapvalue=0;
    String nodename="";
    int nodeint=0; //alternative to nodename
    char[] seqseq=new char[seqlength];
    int[] nodeaddress=new int[0];
    int tips=0;
    int nodes=0;
    int children=nodes+tips;
    float distancetoparent=0;
    Vector kidsvector=new Vector();
    node[] kids=new node[0];
    
    
    void removekid(node remnode){
        int kidsnumber=haskids();
        kidsvector=new Vector();
        for (int i=0;i<kidsnumber;i++){
            if((remnode.getnodename().equalsIgnoreCase(getkids(i).getnodename()))!=true){// if the name of the node to remove does not equal the name of a kidnode..
                kidsvector.addElement(getkids(i));
            }// end if
        }// end for
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);
    }
    
    void removekid(String nodename){
        int kidsnumber=haskids();
        kidsvector=new Vector();
        for (int i=0;i<kidsnumber;i++){
            if((nodename.equalsIgnoreCase(getkids(i).getnodename()))!=true){// if the name of the node to remove does not equal the name of a kidnode..
                kidsvector.addElement(getkids(i));
            }// end if
        }// end for
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);
    }
    
    void removekid(int remint){
        int kidsnumber=haskids();
        kidsvector=new Vector();
        for (int i=0;i<kidsnumber;i++){
            if(i!=remint){// if the kidnumber is not the one to remove
                kidsvector.addElement(getkids(i));
            }// end if
        }// end for
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);
    }
                
    void addkid(node setnode){
        kidsvector.addElement(setnode);
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);
    }// end addkids
    
    String[] gettipnodenames(){
        int kidsnumber=haskids();
        String[] returnarray;
        Vector returnvector=new Vector();
        if(kidsnumber>0){
            for (int i=0; i<kidsnumber;i++){
                if(getkids(i).haskids()==0){
                    returnvector.addElement(getkids(i).getnodename());
                }else{
                    String[] gotarray=getkids(i).gettipnodenames();
                    for(int j=0;j<Array.getLength(gotarray);j++){
                        returnvector.addElement(gotarray[j]);
                    }// end for
                }// end else
            }// end for
        }else{//if kidsnumber=0; i.e. this is a tipnode itself
            returnvector.addElement(getnodename());
        }// end else
        returnarray=new String[returnvector.size()];
        returnvector.copyInto(returnarray);
        return returnarray;
    }//end gettipnodenames
    
    node[] getkidstips(){
        int kidsnumber=haskids();
        node[] returnarray;
        Vector returnvector=new Vector();
        for (int i=0;i<kidsnumber;i++){
            if(getkids(i).haskids()==0){
                returnvector.addElement(getkids(i));
            }else{
                node[] gotarray=getkids(i).getkidstips();
                for (int j=0;j<Array.getLength(gotarray);j++){
                    returnvector.addElement(gotarray[j]);
                }// end for
            }// end else
        }// end for
        returnarray=new node[returnvector.size()];
        returnvector.copyInto(returnarray);
        return returnarray;
    }
            
    int getdescendanttips(){
        int kidsnumber=haskids();
        int alltips=gettips();//get tips of this node
        for(int i=0;i<kidsnumber;i++){//check the descendant nodes
            alltips+=getkids(i).getdescendanttips();//add what they return to the current value
        }// end for
        return alltips;
    }// end getdescendanttips
    
    int getdescendantnodes(){
        int kidsnumber=haskids();
        int allnodes=getnodes();
        for (int i=0; i<kidsnumber;i++){
            allnodes+=getkids(i).getdescendantnodes();
        }//end for
        return allnodes;
    }//end getdescendantnodes
    
    int getdescendantsall(){
        int kidsnumber=haskids();
        int allnodes=0;
        int alltips=0;
        allnodes=getdescendantnodes();
        alltips=getdescendanttips();
        int descendantsall=allnodes+alltips;
        return descendantsall;
    }//end getdescendantsall
    
    float getmaxdistance(){//no negative distances
        int kidsnumber=haskids();
        float maxdistance=0;
        float kidsmaxdistance=0;
        maxdistance=getdistancetoparent();//set the value for this node
        if(maxdistance<0){
            maxdistance=0;
        }
        for (int i=0; i<kidsnumber;i++){//look up the dist value for the kids
            float kidsdistance=getkids(i).getmaxdistance();
            if(kidsdistance>kidsmaxdistance){//the kid with the longestdistance is set as max
                kidsmaxdistance=kidsdistance;
            }
        }// end for
        maxdistance+=kidsmaxdistance;//add the kids distance to the one of the parent node
        return maxdistance;
    }// end getmaxdistance
    
    void setkids(int kidnumber, node setnode){
        kidsvector.setElementAt(setnode,(kidnumber));//Set the kid with the address of the second element
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);//recreate the array
    }// end setkids
    
    void setnewkids(int kidnumber,node setnode){
        kidsvector.insertElementAt(setnode,(kidnumber));//Set the kid with the address of the second element
        kids=new node[kidsvector.size()];
        kidsvector.copyInto(kids);//recreate the array, only one element longerkids[kidnumber]=setnode;
    }// end setnewkids
    
    node getkids(int kidnumber){
        return kids[kidnumber];
    }// end getkids
    
    node[] getkids(){
        return kids;
    }// end getkids
    
    void setnode(int[] address, node setnode){
        int addresslength=Array.getLength(address);
        if (addresslength<=1){
            graph.message("No Node to Set, Wrong Address");
        }
        if (addresslength==2){//if I called this parent and want to set it's child
            kidsvector.setElementAt(setnode,(address[1]));//Set the kid with the address of the second element
            kids=new node[kidsvector.size()];
            kidsvector.copyInto(kids);//recreate the array, only one element longer
        }else{
            int[] newaddress= new int[addresslength-1];
            for(int i=0;i<addresslength-1;i++){// create a new address that is one element shorter
                newaddress[i]=address[i+1];
            }// end for
            kids[newaddress[0]].setnode(newaddress,setnode);
        }
    }// end setnode
        
        
    node getnode(int[] address){
        int addresslength=Array.getLength(address);
        if (addresslength<=1){
            graph.message("No Node to Return, Wrong Address");
        }
        if (addresslength==2){//if I called this parent and want it's child
            return kids[address[1]];//return the kid with the address of the second element
        }else{
            int[] newaddress= new int[addresslength-1];
            for(int i=0;i<addresslength-1;i++){// create a new address that is one element shorter
                newaddress[i]=address[i+1];
            }// end for
            node returnnode=kids[newaddress[0]].getnode(newaddress);
            return returnnode;
        }
    }// end getnode
        
    
    int getbootstrap(){
        return bootstrapvalue;
    }
    void setbootstrap(int i){
        bootstrapvalue=i;
    }
    int haskids(){
        return Array.getLength(kids);
    }
    float getdistancetoparent(){
        if(distancetoparent<0){
            distancetoparent=0;
        }
        return distancetoparent;
    }
    void setdistancetoparent(float f){
        distancetoparent=f;
    }
    int getnodeint(){
        return nodeint;
    }
    void setnodeint(int i){
        nodeint=i;
    }
    int getseqlength(){
        seqlength=Array.getLength(seqseq);
        return seqlength;
    }
    void setseqlength(int setint){
        seqlength=setint;
    }
    String getnodename(){
        return nodename;
    }
    void setnodename(String s){
        nodename=s;
    }
    void setnodename(int i){
        nodename=String.valueOf(i);
    }
    char[] getseq(){
        return seqseq;
    }
    char getseq(int i){
        return seqseq[i];
    }
    void setseq(char[] seqarr){
        seqseq=seqarr;
    }
    void setseq(int i, char c){
        seqseq[i]=c;
    }
    int[] getnodeaddress(){
        return nodeaddress;
    }
    int getnodeaddress(int i){
        return nodeaddress[i];
    }
    void setnodeaddress(int[] iarr){
        nodeaddress=iarr;
    }
    void setnodeaddress(int i,int iarr){
        nodeaddress[i]=iarr;
    }
    int gettips(){
        int tipvalue=0;
        int nodevalue=0;
        for (int i=0;i<Array.getLength(kids);i++){
            int value=kids[i].haskids();
            if(value==0){
                tipvalue+=1;
            }
            if(value>=2){
                nodevalue+=1;
            }
        }
        tips=tipvalue;
        nodes=nodevalue;
        children=tips+nodes;
        return tips;
    }
    void settips(int i){
        tips=i;
    }
    int getnodes(){
        int tipvalue=0;
        int nodevalue=0;
        for (int i=0;i<Array.getLength(kids);i++){
            int value=kids[i].haskids();
            if(value==0){
                tipvalue+=1;
            }
            if(value>=2){
                nodevalue+=1;
            }
        }
        tips=tipvalue;
        nodes=nodevalue;
        children=tips+nodes;
        return nodes;
    }
    /*void setnodes(int i){
        nodes=i;
    }*/
    int getchildren(){
        int tipvalue=0;
        int nodevalue=0;
        for (int i=0;i<Array.getLength(kids);i++){
            int value=kids[i].haskids();
            if(value==0){
                tipvalue+=1;
            }
            if(value>=2){
                nodevalue+=1;
            }
        }
        tips=tipvalue;
        nodes=nodevalue;
        children=nodes+tips;
        return children;
    }// no setchildren method because children=nodes+tips
    
    
    
    
    
    
    
    
    
    
    
}// end class node