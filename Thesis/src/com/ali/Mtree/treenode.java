package com.ali.Mtree;

/*
 * treenode.java
 *
 * Created on December 6, 2001, 7:28 PM
 */

/**
 *
 * @author  noname
 * @version
 */
public class treenode {
    
    /** Creates new treenode */
    public treenode() {
    }
    
    String name="";
    float bootstrap=(float)0;
    float length=(float)0;
    treenode[] kids=new treenode[0];
    
    int kids(){
        return java.lang.reflect.Array.getLength(kids);
    }// end kids
    
    int getdescendants(){
        int counter=kids();
        if(counter>0){
            for(int i=0; i<kids();i++){
                counter+=kids[i].getdescendants();
            }// end for
        }// end if
        return counter;
    }// end getdescendants
    
    boolean haskid(String inname){
        if(name.equals(inname)){
            return true;
        }
        for(int i=0;i<kids();i++){
            if(kids[i].haskid(inname)){
                return true;
            }
        }// end for
        return false;
    }// end haskid
    
    boolean haskidpart(String inname){
        if(name.indexOf(inname)!=-1){
            return true;
        }
        for(int i=0;i<kids();i++){
            if(kids[i].haskidpart(inname)){
                return true;
            }
        }// end for
        return false;
    }// end haskidpart
    
    int gettipnodes(){
        int outval=0;
        if(kids()==0){
            return 1;
        }
        for (int i=0;i<kids();i++){
            outval+=kids[i].gettipnodes();
        }
        return outval;
    }// end gettipnodes
    
    String[] gettipnames(){
        String[] outarr=new String[0];
        String[] newarr;
        if(kids()==0){
            outarr=new String[1];
            outarr[0]=name;
            return outarr;
        }
        for(int i=0;i<kids();i++){
            String[] tmparr=kids[i].gettipnames();
            int outlength=java.lang.reflect.Array.getLength(outarr);
            int tmplength=java.lang.reflect.Array.getLength(tmparr);
            newarr=new String[outlength+tmplength];
            for(int j=0;j<outlength;j++){
                newarr[j]=outarr[j];
            }// end for j
            for(int j=0;j<tmplength;j++){
                newarr[outlength+j]=tmparr[j];
            }// end for j
            outarr=newarr;
        }// end for i
        return outarr;
    }
    
    void removekid(treenode innode){// doesn't check wether innode really is a kidnode
        treenode[] newkids=new treenode[kids()-1];
        int skip=0;
        boolean thisskip;
        for(int i=0;i<kids();i++){
            thisskip=false;
            if(innode.name.equals(kids[i].name)){
                skip+=1;
                thisskip=true;
            }
            if(thisskip==false){
                newkids[i-skip]=kids[i];
            }
        }// end for
        kids=newkids;
    }// end removekid
    
    void addkids(treenode[] newkids){
        int newnumber=java.lang.reflect.Array.getLength(newkids);
        int oldnumber=java.lang.reflect.Array.getLength(kids);
        int skip=0;
        //check for duplicate kidnodes
        if((oldnumber>0)&&(newnumber>0)){
            for(int i=0;i<newnumber;i++){
                for(int j=0;j<oldnumber;j++){
                    if(newkids[i].equals(kids[j])){
                        newkids[i].name="noadd";
                        skip+=1;
                    }
                }// end for j
            }// end for i
        }// end if
        treenode[] tmp=new treenode[oldnumber+newnumber-skip];
        skip=0;
        for(int i=0;i<(oldnumber+newnumber);i++){
            if(i<oldnumber){
                tmp[i]=kids[i];
            }
            if(i>=oldnumber){
                if(newkids[i-oldnumber].name.equals("noadd")){
                    skip+=1;
                }
                if((newkids[i-oldnumber].name.equals("noadd"))==false){
                    tmp[i]=newkids[i-oldnumber-skip];
                }
            }
        }// end for
        kids=tmp;
    }// end addkids
    
    void addkid(treenode innode){
        treenode[] newkids=new treenode[kids()+1];
        for(int i=0;i<kids();i++){
            newkids[i]=kids[i];
        }// end for
        newkids[kids()]=innode;
        kids=newkids;
    }
    
    float getboot(){
        if(kids()==0){// if this is a tipnode
            return -1;
        }
        return bootstrap;
    }// end getboot
}
