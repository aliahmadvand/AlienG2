package com.ali.Mtree;

/*
 * methods.java
 *
 * Created on December 6, 2001, 6:20 PM
 */

/**
 *
 * @author  noname
 * @version
 */
public class methodsnograph_bkup {
    
    /** Creates new methods */
    public methodsnograph_bkup() {
    }
    
    treenode computetree(double[][][] indistances,String[] innames){
        treenode orgnode=new treenode();
        int replicates=java.lang.reflect.Array.getLength(indistances)-1;
        int species=java.lang.reflect.Array.getLength(innames);
        int species2=java.lang.reflect.Array.getLength(indistances[0]);
        if(species!=species2){
            orgnode.name="unequal species number";
            return orgnode;
        }
        //if there are two or less species
        if(species<3){
            treenode[] tmpnodes=new treenode[species+1];
            tmpnodes[0]=new treenode();
            for(int i=1;i<species+1;i++){
                tmpnodes[i]=new treenode();
                tmpnodes[i].name=innames[i-1];
                tmpnodes[0].addkid(tmpnodes[i]);
            }// end for i
            return tmpnodes[0];
        }// end if species<3
        if(species==3){
            treenode outnode;
            treenode[] nodes=new treenode[3];
            for(int j=0;j<3;j++){
                nodes[j]=new treenode();
                nodes[j].name=new String(innames[j]);
            }
            double[][] currmtx=indistances[0];
            return makelastnode(nodes,currmtx);
        }// end if species=3
        treenode[] treearr=new treenode[replicates+1];
        for(int i=0;i<replicates+1;i++){
            treenode[] nodes=new treenode[species];
            for(int j=0;j<species;j++){
                nodes[j]=new treenode();
            }
            for(int j=0;j<species;j++){
                nodes[j].name=new String(innames[j]);
            }// end for i
            int cnt=1;
            double[][] currmtx=indistances[i];
            while(java.lang.reflect.Array.getLength(currmtx)>3){
                double[] diverg=getalldiverg(currmtx);
                double[][] corrmtx=ratescorrect(currmtx,diverg);
                int[] group=getmindiverg(corrmtx);
                nodes=makenextnode(nodes,group,currmtx,diverg,cnt);
                currmtx=updatemtx(currmtx,group);
                cnt+=1;
            }// end while
            treearr[i]=makelastnode(nodes,currmtx);
        }// end for i
        orgnode=computebootstrap(treearr);
        return orgnode;
    }// end computetree
    
    treenode computebootstrap(treenode[] inarr){
        treenode orgnode=inarr[0];
        for(int i=0;i<orgnode.kids();i++){
            orgnode.kids[i]=computebootstrap(orgnode.kids[i],inarr);
            orgnode.kids[i].bootstrap=orgnode.kids[i].bootstrap-1;//have orgnode in the array
        }// end for i
        return orgnode;
    }// end computebootstrap
    
    treenode computebootstrap(treenode innode, treenode[] inarr){
        if(innode.kids()==0){
            return innode;
        }
        for(int i=0;i<innode.kids();i++){
            innode.kids[i]=computebootstrap(innode.kids[i],inarr);
            innode.kids[i].bootstrap=innode.kids[i].bootstrap-1;//have orgnode in the array
        }// end for i
        int replicates=java.lang.reflect.Array.getLength(inarr);
        String[] tipnames=innode.gettipnames();
        for(int i=0;i<replicates;i++){
            if(hasnodewithtips(inarr[i],tipnames)){
                innode.bootstrap+=1;
            }
        }// end for i
        return innode;
    }// end computebootstrap
    
    boolean hasnodewithtips(treenode innode,String[] namesarr){
        String[] thistips=innode.gettipnames();
        boolean istrue=false;
        int thislength=java.lang.reflect.Array.getLength(thistips);
        int oldlength=java.lang.reflect.Array.getLength(namesarr);
        if(thislength==oldlength){
            boolean allequal=true;
            for(int i=0;i<oldlength;i++){
                boolean hasequal=false;
                for(int j=0;j<thislength;j++){
                    if(namesarr[i].equals(thistips[j])){
                        hasequal=true;
                    }
                }// end for j
                if(hasequal==false){
                    allequal=false;
                }
            }// end for i
            return allequal;
        }// end if length equals
        for(int i=0;i<innode.kids();i++){
            if(istrue==false){
                istrue=hasnodewithtips(innode.kids[i],namesarr);
            }
        }// end for i
        return istrue;
    }// end hasnodewithtips
    
    treenode makelastnode(treenode[] innodes, double[][] distmtx){
        int individuals=java.lang.reflect.Array.getLength(innodes);//==3
        double[] diverg=getalldiverg(distmtx);
        double[][] corrmtx=ratescorrect(distmtx,diverg);
        treenode returnnode=new treenode();
        returnnode.name="lastnode";
        int[] groupab=getmindiverg(corrmtx);
        int a=groupab[0];
        int b=groupab[1];
        if((a==0)&&(b==0)){
            a=0;
            b=1;
        }
        int c=(3-(a+b));//(3=a+b+c, 0+1+2)
        double sau=(distmtx[a][b]/2)+(diverg[a]-diverg[b])/(2*(individuals-2));// compute the distance of the tip to the parent node
        double sbu=(distmtx[b][a]/2)+(diverg[b]-diverg[a])/(2*(individuals-2));
        double scu=(distmtx[a][c]+distmtx[b][c]-distmtx[a][b])/2;
        innodes[a].length=((float)sau);
        innodes[b].length=((float)sbu);
        innodes[c].length=((float)scu);
        returnnode.addkid(innodes[a]);
        returnnode.addkid(innodes[b]);
        returnnode.addkid(innodes[c]);
        return returnnode;
    }// end makelastnode
    
    double[][] updatemtx(double[][] currdistmtx,int[] ingroup){
        int a=ingroup[0];// a is smaller than b
        int b=ingroup[1];
        int individuals=java.lang.reflect.Array.getLength(currdistmtx);
        double[][] newdistmtx=new double[individuals-1][individuals-1];
        for(int i=0;i<individuals;i++){//reassign new values to a distance matrix that is one collumn and one row shorter
            for(int j=0;j<individuals;j++){
                if(i>b){// all data has to be shifted by one
                    if(j>b){// all data has to be shifted by one
                        newdistmtx[i-1][j-1]=currdistmtx[i][j];
                    }
                    if(j==b){// do nothing
                    }
                    if(j==a){
                        newdistmtx[i-1][j]=(currdistmtx[i][a]+currdistmtx[i][b]-currdistmtx[a][b])/2;
                    }
                    if((j<b)&&(j!=a)) {
                        newdistmtx[i-1][j]=currdistmtx[i][j];//default:j<b&&j!=a
                    }
                }//end if i>b
                if(i==a){
                    // the new values have to be computed for i
                    if(j>b){// all data has to be shifted by one
                        newdistmtx[i][j-1]=(currdistmtx[a][j]+currdistmtx[b][j]-currdistmtx[a][b])/2;;
                    }
                    if(j==b){// do nothing
                    }
                    if(j==a){
                        newdistmtx[i][j]=(currdistmtx[i][a]+currdistmtx[i][b]-currdistmtx[a][b])/2;
                    }
                    if((j<b)&&(j!=a)){
                        newdistmtx[i][j]=(currdistmtx[a][j]+currdistmtx[b][j]-currdistmtx[a][b])/2;//default:j<b&&j!=a
                    }
                }//end if i==a
                if(i==b){
                    //do nothing, the b&a-node values are now in the former position of a
                }
                if((i<b)&&(i!=a)){
                    if(j>b){// all data has to be shifted by one
                        newdistmtx[i][j-1]=currdistmtx[i][j];
                    }
                    if(j==b){// do nothing
                    }
                    if(j==a){
                        newdistmtx[i][j]=(currdistmtx[i][a]+currdistmtx[i][b]-currdistmtx[a][b])/2;
                    }
                    if((j<b)&&(j!=a)){
                        newdistmtx[i][j]=currdistmtx[i][j];//default:j<b&&j!=a
                    }
                }// end i<b&&i!=a
                
            }// end for j
        }// end for i
        if(individuals>3){// unless I'm in the step before last update matrix
            currdistmtx=newdistmtx;
        }
        if(individuals<=3){
            System.out.println("something funny going on, indiv<=3");
        }
        return currdistmtx;
    }// end makenewcurrdistmtx
    
    treenode[] makenextnode(treenode[] innodes,int[] ingroup,double[][] inmtx,double[] diverg,int cnt){
        treenode newnode=new treenode();
        treenode[] outnodes=new treenode[java.lang.reflect.Array.getLength(innodes)-1];
        newnode.name=String.valueOf(cnt);
        newnode.addkid(innodes[ingroup[0]]);
        newnode.addkid(innodes[ingroup[1]]);
        newnode.kids[0].length=(float)((inmtx[ingroup[0]][ingroup[1]]/2)+(diverg[ingroup[0]]-diverg[ingroup[1]])/(2*(java.lang.reflect.Array.getLength(innodes)-2)));
        if(newnode.kids[0].length<0){//do not allow negative branch lengths
            System.err.println("neg. branch lengths, setting to zero in "+newnode.name);
            newnode.kids[0].length=0;
        }
        newnode.kids[1].length=(float)(inmtx[ingroup[0]][ingroup[1]]-newnode.kids[0].length);
        if(newnode.kids[1].length<0){//do not allow negative branch lengths
            System.err.println("neg. branch lengths, setting to zero in "+newnode.name);
            newnode.kids[1].length=0;
        }
        int spacer=0;
        for(int i=0;i<java.lang.reflect.Array.getLength(outnodes);i++){
            if(i==ingroup[0]){
                outnodes[i]=newnode;
            }
            if(i==ingroup[1]){
                spacer=1;
            }
            if(i!=ingroup[0]){
                outnodes[i]=innodes[i+spacer];
            }
        }// end for i
        return outnodes;
    }// end makenextnode
    
    int[] getmindiverg(double[][] inmtx){
        double minval=java.lang.Double.MAX_VALUE;
        int[] group=new int[2];
        int elements=java.lang.reflect.Array.getLength(inmtx);
        for(int i=0;i<elements;i++){
            for(int j=i+1;j<elements;j++){
                if(inmtx[i][j]<minval){
                    group[0]=i;
                    group[1]=j;
                    minval=inmtx[i][j];
                }// end if
            }// end for j
        }// end for i
        return group;
    }// end getmindiverg
    
    double[][] ratescorrect(double[][] inmtx,double[] diverg){
        int individuals=java.lang.reflect.Array.getLength(inmtx);
        double[][] corrmtx=new double[individuals][individuals];
        for(int i=0; i<individuals;i++){
            for (int j=i;j<individuals;j++){
                corrmtx[i][j]=inmtx[i][j]-((diverg[i]+diverg[j])/(individuals-2));
                corrmtx[j][i]=corrmtx[i][j];
            }// end for
        }// end for
        return corrmtx;
    }// end ratescorrect
    
    double[] getalldiverg(double[][] inarr){
        int elements=java.lang.reflect.Array.getLength(inarr);
        double[] outarr=new double[elements];
        for(int i=0;i<elements;i++){
            for(int j=0;j<elements;j++){
                outarr[i]+=inarr[i][j];
            }// end for j
        }// end for
        return outarr;
    }// end getalldiverg
    
    double[][] getdistances(char[][] inarr,String corrmet, treemakethreadnograph parent){
        int species=java.lang.reflect.Array.getLength(inarr);
        double[][] outarr=new double[species][species];
        int seqlength=java.lang.reflect.Array.getLength(inarr[0]);
        for(int i=0;i<species;i++){
            for(int j=i+1;j<species;j++){
                int counter=0;
                int skipped=0;
                //System.out.println("getting distance for "+new String(inarr[i])+" and "+new String(inarr[j]));
                for(int k=0;k<seqlength;k++){
                    if((inarr[i][k]!='-')&&(inarr[j][k]!='-')&&(inarr[i][k]!=inarr[j][k])){
                        counter+=1;
                    }else if((inarr[i][k]=='-')||(inarr[j][k]=='-')){
                        skipped+=1;
                    }
                }// end for k
                if(corrmet.equalsIgnoreCase("poisson")){
                    outarr[i][j]=distance.poisson((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("kimura")){
                    outarr[i][j]=distance.kimura((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("tajimanei")){
                    outarr[i][j]=distance.tajimanei((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("none")){
                    outarr[i][j]=(double)((double)counter/((double)(seqlength-skipped)));
                }else{
                    System.err.println("unknown distance correction method "+corrmet+" will use no correction");
                    parent.corrmet="none";
                    corrmet="none";
                    outarr[i][j]=(double)((double)counter/((double)(seqlength-skipped)));
                }
                if((java.lang.Double.isNaN(outarr[i][j]))||(java.lang.Double.isInfinite(outarr[i][j]))){
                    parent.skipthis=true;
                    System.err.println("NaN or Infinite for getbootdistance("+i+","+j+") in "+parent.dofile.getName()+" will try new replicate");
                    outarr[i][j]=java.lang.Double.MAX_VALUE;
                }
                outarr[j][i]=outarr[i][j];
            }// end for j
            outarr[i][i]=0;
        }// end for i
        return outarr;
    }// end getdistances
    
    double[][] getdistances(aaseq[] inarr,String corrmet,treemakethreadnograph parent){
        //System.out.println(corrmet);
        int species=java.lang.reflect.Array.getLength(inarr);
        double[][] outarr=new double[species][species];
        int seqlength=java.lang.reflect.Array.getLength(inarr[0].seqseq);
        for(int i=0;i<species;i++){
            for(int j=i+1;j<species;j++){
                int counter=0;
                int skipped=0;
                for(int k=0;k<seqlength;k++){
                    if((inarr[i].seqseq[k]!='-')&&(inarr[j].seqseq[k]!='-')&&(inarr[i].seqseq[k]!=inarr[j].seqseq[k])){
                        counter+=1;
                    }
                    if((inarr[i].seqseq[k]=='-')||(inarr[j].seqseq[k]=='-')){
                        skipped+=1;
                    }
                }// end for k
                if(corrmet.equalsIgnoreCase("poisson")){
                    outarr[i][j]=distance.poisson((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("kimura")){
                    outarr[i][j]=distance.kimura((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("tajimanei")){
                    outarr[i][j]=distance.tajimanei((double)((double)counter/(double)(seqlength-skipped)));
                }else if(corrmet.equalsIgnoreCase("none")){
                    outarr[i][j]=(double)((double)counter/((double)(seqlength-skipped)));
                }else{
                    System.err.println("unknown distance correction method "+corrmet+" will use no correction");
                    corrmet="none";
                    outarr[i][j]=(double)((double)counter/((double)(seqlength-skipped)));
                }
                if((java.lang.Double.isNaN(outarr[i][j]))||(java.lang.Double.isInfinite(outarr[i][j]))){
                    parent.skipthis=true;
                    System.err.println("NaN or Infinite for getdistance("+i+","+j+") in "+parent.dofile.getName());
                    outarr[i][j]=java.lang.Double.MAX_VALUE;
                }
                outarr[j][i]=outarr[i][j];
            }// end for j
            outarr[i][i]=0;
        }// end for i
        return outarr;
    }// end getdistances
    
    char[][] makereplicate(aaseq[] inarr,java.util.Random rand){
        int species=java.lang.reflect.Array.getLength(inarr);
        int seqlength=java.lang.reflect.Array.getLength(inarr[0].seqseq);
        //System.out.println("replicate input:species="+species+" seqlength="+seqlength);
        char[][] chararray=new char[species][seqlength];
        int j;
        int currpos;
        for(int i=0;i<seqlength;i++){
            currpos=rand.nextInt(seqlength);
            for(j=0;j<species;j++){
                chararray[j][i]=inarr[j].seqseq[currpos];
            }// end for j
        }// end for
        return chararray;
    }// end makereplicate
    
    // rerooting trees
    
    treenode lastparent;
    float lastlength;
    float lastboot;
    float lastlastboot;
    treenode newroot=new treenode();
    treenode mostnested=new treenode();
    int maxlvl=0;
    boolean foundroot=false;
    boolean foundroototu=false;//does this roototu exist at all in this tree?
    
    public treenode roottree(treenode innode,String rootname){
        foundroototu=false;
        innode=rootnode(innode,rootname);//in this part newroot is defined and updated
        if(foundroototu==false){
            System.err.println("unable to find root species "+rootname);
            return innode;
        }
        if(innode.kids()<=1){//if there is only one kid left
            // remove innode and add innode.kids to innode's parentnode
            innode.kids[0].length=innode.kids[0].length+lastlength;
            lastparent.addkids(innode.kids);
            lastparent.removekid(innode);
            return newroot;
        }
        //System.out.println("kids>1"+lastboot);
        innode.bootstrap=lastlastboot;
        return newroot;
    }// end roottree
    
    private treenode rootnode(treenode innode,String rootname){//root current tree with node with name rootname
        treenode tmpnode=new treenode();
        foundroot=false;
        if(innode.name.indexOf(rootname)!=-1){
            newroot=new treenode();
            newroot.name="newroot";
            newroot.addkid(innode);
            foundroot=true;
            foundroototu=true;
            return newroot;
        }
        int rootkid=-1;
        // if foundroot == true should never be the starting case for this sub.
        if(foundroot==false){// but check nevertheless
            for(int i=0;i<innode.kids();i++){
                if(foundroot==false){
                    innode.kids[i]=rootnode(innode.kids[i],rootname);
                }
                if(foundroot==true){
                    rootkid=i;
                    i=java.lang.Integer.MAX_VALUE-1;// terminate the for loop
                }
            }// end for
            if(foundroot==false){
                return innode;
            }
            if(foundroot==true){
                float tmp=innode.length;
                innode.length=lastlength;
                lastlength=tmp;
                lastlastboot=lastboot;
                lastboot=innode.getboot();
                tmpnode=innode.kids[rootkid];
                innode.removekid(tmpnode);
                lastparent=tmpnode;
                tmpnode.addkid(innode);
                return innode;
            }
        }// end if foundroot==false
        // should never get to here!
        System.err.println("error in rerooting node "+innode.name+" foundroot="+foundroot);
        return innode;
    }// end rootnode
    
}// end class
