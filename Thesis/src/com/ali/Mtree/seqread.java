package com.ali.Mtree;

/*
 * seqread.java
 *
 * Created on September 8, 2000, 10:59 AM
 */

//package rates;
import java.lang.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
/**
 *
 * @author  noname
 * @version 
 */
public class seqread extends Object {

    /** Creates new seqread */
    public seqread() {
    }

//*****************************************************************************
    //fastaread reads in an alignment of fasta alignment format
    
public static aaseq[] fastaread(File filenamein)
    throws IOException{
        String aaseqname;//needed for aaseq object
        Vector seqvector=new Vector();//will hold the aaseq objects, number unknown
        aaseq seqobj=new aaseq();
        BufferedReader alignread = dataf.in(filenamein);
        int seqlength=0;//needed for the aaseq object
        int readint=0, speciesnumber=0;
        int seqposition=0;
        char readchar=' ';
        boolean firstpass=true;
        aaseq[] seqarrayout = new aaseq[0];
        boolean readon=true;
        Vector sequence=new Vector();
        aaseq currentread=new aaseq();
        Character[] aasequence;
        int currseq=0;
        while (readon){
            readint=alignread.read();//read in a character  
            readchar=(char)readint;  //convert what you read to a char
            try{                                    //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
            if(firstpass){
                if (readchar!='>'){
                    graph.error("File not in FASTA Format");
                    return seqarrayout;
                }
                firstpass=false;
            }
            
            if(readchar=='>'){
               /* if (speciesnumber==0){//if no sequence has yet been read in
                    currentread=new aaseq();
                    sequence=new Vector();
                    seqposition=0;
                }*/
                currseq++;
                if (speciesnumber>0){//if a sequence has already been read in
                    if(speciesnumber==1){
                        seqlength=seqposition;
                    }
                    if((speciesnumber>1)&&(seqlength!=seqposition)){
                        graph.error("Alignment error in fastaread! ERROR:different lengths for 0 and "+currseq);
                    }
                    aasequence = new Character[sequence.size()];
                    sequence.copyInto(aasequence);
                    char[] charsequence=new char[Array.getLength(aasequence)];
                    for(int i=0;i<(Array.getLength(aasequence));i++){
                        charsequence[i]=(aasequence[i].charValue());
                    }
                    currentread.seqseq=charsequence;
                    seqvector.addElement(currentread);
                    currentread=new aaseq();
                    sequence=new Vector();
                    seqposition=0;
                }
            currentread.seqname=alignread.readLine();//read in what comes after the ">" and make it the seqname
            speciesnumber++;
            }//end if readchar =='>'
            if(readchar!='>'){
                if ((Character.isLetterOrDigit(readchar))||(readchar=='-')||(readchar=='?')){
                    if(readchar=='?'){
                        readchar='-';
                    }
                    Character readCharacter=new Character(readchar);
                    sequence.addElement(readCharacter);
                    seqposition++;
                }
            }//end if readchar!=>
            //readint=alignread.read();
        }//end readon
        alignread.close();
        if((speciesnumber>1)&&(seqlength!=seqposition)){
                        graph.error("Alignment error in fastaread! ERROR:different lengths");
                    }
                    aasequence = new Character[sequence.size()];
                    sequence.copyInto(aasequence);
                    char[] charsequence=new char[Array.getLength(aasequence)];
                    for(int i=0;i<(Array.getLength(aasequence));i++){
                        charsequence[i]=(aasequence[i].charValue());
                    }
                    currentread.seqseq=charsequence;
                    seqvector.addElement(currentread);
        seqarrayout = new aaseq[seqvector.size()];
        seqvector.copyInto(seqarrayout);
        //testseqvector(seqarrayout);
        return seqarrayout; 
        
}//end fastaread
 
//*************************************************************
//clustalintread reads seqs in clustal interleaved format

public static aaseq[] clustalintread(File filenamein)
throws IOException{
        System.out.println("reading from file "+filenamein);
        Vector seqvector=new Vector();//will hold the aaseq objects, number unknown
        BufferedReader alignread = dataf.in(filenamein);
        int seqlength=0;//needed for the aaseq object
        int speciesnumber=0;
        int seqposition=0;
        char readchar=0;
        aaseq[] seqarrayout=new aaseq[0];
        boolean readon=true;
        boolean newline=false;
        Character[] aasequence;
        String header= new String();
        header=alignread.readLine().trim();//should be clustal + maybe some other noninformative stuff
        try{
            if((header.indexOf("clustal")==-1)&&(header.indexOf("Clustal")==-1)&&(header.indexOf("CLUSTAL")==-1)){
                graph.error("Alignment not in Clustal Format");
                return seqarrayout;
            }
        }catch (java.lang.StringIndexOutOfBoundsException e){graph.error("Alignment not in correct Clustal Format");}
        newline=true;
        //System.out.println("starting clustalseqread");
        try{                                    //see if there is more to read
            boolean readable=alignread.ready();
            if(readable!=true){
                readon=false;
            }
        }catch (IOException e){
            readon=false;
            graph.message("EOF reached");
        }
        //System.out.println("can read sth");
        while (readon){
            alignread.mark(2);
            readchar=(char)alignread.read();
            while((Character.isWhitespace(readchar))||(readchar=='.')||(readchar=='*')||(readchar==':')){//if there was a newline or a space or "." or ":" or "*".
                    alignread.mark(2);//save the current position
                readchar=(char)alignread.read();
                newline=true;
            }//if another newline repeat loop
            alignread.reset();//if not, start reading after the last mark;
        
            if(newline==true){
                aaseq currentread=new aaseq();
                newline=false;//I' reading a new species
                Vector seqname=new Vector();
                try{                                    //see if there is more to read
                    boolean readable=alignread.ready();
                    if(readable!=true){
                        readon=false;
                    }
                }catch (IOException e){
                    readon=false;
                    graph.message("EOF reached");
                }
                readchar=(char)alignread.read();
                while(((Character.isWhitespace(readchar))!=true)){//&&(readchar!='.')&&(readchar!='*')&&(readchar!=':')){
                    Character namechar=new Character(readchar);
                    seqname.addElement(namechar);
                    readchar=(char) alignread.read();
                    
                }//end while
                Character[] dummyarray= new Character[seqname.size()];//need to do this because I can't copy straight into a char array
                char[] namechararr= new char[Array.getLength(dummyarray)];
                seqname.copyInto(dummyarray);
                for (int i=0;i<(Array.getLength(dummyarray));i++){//convert the Character array to char array
                    namechararr[i]=dummyarray[i].charValue();
                }//end for
                String aaseqname = new String(namechararr);//make a string out of it
                currentread.seqname=aaseqname;
                //System.out.println("name=\""+currentread.getname()+"\"");
                //*************************done namereadin*************************************
                alignread.mark(2);
                readchar=(char)alignread.read();
                while((Character.isWhitespace(readchar))||(readchar=='.')||(readchar=='*')){//if there was a newline or a space
                    alignread.mark(2);//save the current position
                    readchar=(char)alignread.read();
                    newline=true;
                }//if another newline repeat loop
                alignread.reset();//if not, start reading after the last mark;
                
                readchar=(char)alignread.read();
                Vector aaseqvector=new Vector();
                seqlength=0;
                while (((Character.isWhitespace(readchar))!=true)&&(readchar!='*')&&(readchar!='.')){
                    Character seqaa=new Character(readchar);
                    aaseqvector.addElement(seqaa);
                    readchar=(char)alignread.read();
                    seqlength+=1;
                }//end while
                Character[] dummyseqarr=new Character[aaseqvector.size()];
                aaseqvector.copyInto(dummyseqarr);
                char[] seqchararray=new char[Array.getLength(dummyseqarr)];
                for(int i=0;i<(Array.getLength(dummyseqarr));i++){
                    seqchararray[i]=dummyseqarr[i].charValue();
                }//end for
                currentread.seqseq=seqchararray;
                seqvector.addElement(currentread);
        
            try{                                    //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
        
            }//end if newline==true
            alignread.mark(2);
            readchar=(char)alignread.read();
            while((Character.isWhitespace(readchar))||(readchar=='.')||(readchar=='*')||(readchar==':')){//if there was a newline or a space
                alignread.mark(2);//save the current position
                readchar=(char)alignread.read();
                newline=true;
            }//if another newline repeat loop
            alignread.reset();//if not, start reading after the last mark;
        
            try{                                    //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
            //System.out.println("can read sth");
        
            }//end while readon 
            aaseq[] seqarraytmp=new aaseq[seqvector.size()];
            seqvector.copyInto(seqarraytmp);
        
            alignread.close();
            //************************read all the stuff from the seqfile*******************
            //now I have to concatenate the diff seq parts from the seqarraytmp array
            Vector convertvector=new Vector();
            String startname=seqarraytmp[0].seqname;
            String firstname=startname;
            int assignloop=0;
            for (int i=0;(i<Array.getLength(seqarraytmp));i++){//move through the array one by one
                firstname=seqarraytmp[i].seqname;
                if(firstname.equalsIgnoreCase(startname)){
                    assignloop+=1;}
                for(int j=i+1;(j<Array.getLength(seqarraytmp))&&(assignloop<2);j++){
                    String secondname=seqarraytmp[j].seqname;
                    //System.out.println("i="+i+" j="+j);
                    if(firstname.equalsIgnoreCase(secondname)==true){
                        int length1=java.lang.reflect.Array.getLength(seqarraytmp[i].seqseq);
                        int length2=java.lang.reflect.Array.getLength(seqarraytmp[j].seqseq);
                        int newlength=length1+length2;
                        char[] tmparray=new char[newlength];
                        for(int k=0;k<newlength;k++){
                            if(k<length1){
                                tmparray[k]=seqarraytmp[i].seqseq[k];
                            }
                            if((k>=length1)&&(k<newlength)){
                                tmparray[k]=seqarraytmp[j].seqseq[k-length1];
                            }
                        }//end for
                        seqarraytmp[i].seqseq=tmparray;
                        
                    }//end if firstname==secondname
                    
                }//end for j
                if (assignloop<2){
                convertvector.addElement(seqarraytmp[i]);
                //System.out.println("name:"+seqarraytmp[i].getname()+" length:"+seqarraytmp[i].getlength());
                }//end if
            }//end for i
                
            
        seqarrayout=new aaseq[convertvector.size()]; 
        convertvector.copyInto(seqarrayout);
        //testseqvector(seqarrayout);
        return seqarrayout;
}//end clustalintread

//*************************************************************
//phylipseqread reads seqs in phylip sequential format

public static aaseq[] phylipseqread(File filenamein)
throws IOException{
    String aaseqname;
    aaseq seqobj;
    int seqlength=0, individuals=0;
    int readloop=0;
    char readchar;
    char[] readinfoarr= new char[7];
    aaseq[] seqarrayout=new aaseq[0];
    BufferedReader alignread=dataf.in(filenamein);
    int readinfo=0;
    while(readinfo<2){
        int readstuff=0;
        readchar=(char)alignread.read();
        while(Character.isDigit(readchar)){
            readinfoarr[readstuff]=readchar;
            readstuff+=1;
            readchar=(char)alignread.read();
        }//end while
        if(readstuff!=0){// if I read something
            if(readinfo==0){
                String stringinfo= new String(readinfoarr,0,readstuff);//make a string out of all chars read
                //System.out.println("stringinfo1="+stringinfo);
                individuals=0;
                try{
                    individuals = Integer.parseInt(stringinfo);
                }catch (NumberFormatException e){graph.message("Invalid Alignment");
                    aaseq[] errorseq = new aaseq[0];    
                    return (errorseq);
                }//return a dummy aaseq array
                    readinfoarr=new char[7];
            }
            if(readinfo==1){
                String stringinfo= new String(readinfoarr,0,readstuff);//make a string out of all chars read
                //System.out.println("stringinfo1="+stringinfo);
                seqlength=0;
                try{
                    seqlength = Integer.parseInt(stringinfo);
                }catch (NumberFormatException e){graph.message("Invalid Alignment");
                    aaseq[] errorseq = new aaseq[0];    
                    return (errorseq);
                }//return a dummy aaseq array
        
            }
             readinfo+=1;
        }//end if readstuff
    }//end while readinfo
    seqarrayout=new aaseq[individuals];
        if((readinfo==0)||(seqlength==0)){
            graph.error("Alignment not in Phylip format");
            return seqarrayout;
        }
       
    while(readloop<individuals){//while I haven't read all individuals
        alignread.mark(2);
        readchar=(char)alignread.read();
        while((Character.isWhitespace(readchar))){//if there was a newline or a space
            alignread.mark(2);//save the current position
            readchar=(char)alignread.read();
        }//if another newline repeat loop
        alignread.reset();//if not, start reading after the last mark;
        aaseq currentread=new aaseq();
        boolean readon=true;
        char[] seqname=new char[10];
        char[] seqseq=new char[seqlength];
        alignread.read(seqname,0,10);//read the first 10 chars and assign them to seqname
        String sequencename=new String(seqname);
        currentread.seqname=String.valueOf(sequencename);
        currentread.seqseq=seqseq;
        seqname=new char[10];
        int seqpos=0;
        while((seqpos<seqlength)&&(readon)){
            alignread.mark(2);
            while((Character.isWhitespace(readchar))){//if there was a newline or a space
                alignread.mark(2);//save the current position
                readchar=(char)alignread.read();
            }//if another newline repeat loop
            alignread.reset();//if not, start reading after the last mark;
            
            readchar=(char)alignread.read();
            if(readchar=='?'){
                readchar='-';
            }
            currentread.seqseq[seqpos]=readchar;
            //System.out.println("pos:"+seqpos+" char:"+readchar);
            seqpos+=1;//increment counter
            if(Character.isWhitespace(readchar)){//if I read in a space or return char
                seqpos-=1;                         //do not increment counter
            }
        }//end while seqpos<seqlength
        seqarrayout[readloop]=currentread;
        readloop+=1;
    }//end while readloop<individuals
    //System.out.println("Done");
    //testseqvector(seqarrayout);
    alignread.close();
    return seqarrayout;
    }//end phylipseqread   
  
//**************************************************************
//phylipread reads seqs in phylip interleaved mode

public static aaseq[] phylipread(File filenamein)
throws IOException{
    String aaseqname;
    aaseq seqobj;
    int seqlength=0, individuals=0;
    char readchar;
    char[] readinfoarr= new char[7];
    BufferedReader alignread=dataf.in(filenamein);
    int readinfo=0;
    while(readinfo<2){
        int readstuff=0;
        readchar=(char)alignread.read();
        while(Character.isDigit(readchar)){
            readinfoarr[readstuff]=readchar;
            readstuff+=1;
            readchar=(char)alignread.read();
        }//end while
        if(readstuff!=0){// if I read something
            if(readinfo==0){
                String stringinfo= new String(readinfoarr,0,readstuff);//make a string out of all chars read
                individuals=0;
                try{
                    individuals = Integer.parseInt(stringinfo);
                }catch (NumberFormatException e){graph.message("Invalid Alignment");
                    aaseq[] errorseq = new aaseq[0];    
                    return (errorseq);
                }//return a dummy aaseq array
                    readinfoarr=new char[7];
            }
            if(readinfo==1){
                String stringinfo= new String(readinfoarr,0,readstuff);//make a string out of all chars read
                seqlength=0;
                try{
                    seqlength = Integer.parseInt(stringinfo);
                }catch (NumberFormatException e){graph.message("Invalid Alignment");
                    aaseq[] errorseq = new aaseq[0];    
                    return (errorseq);
                }//return a dummy aaseq array
        
            }
             readinfo+=1;
        }//end if readstuff
    }//end while readinfo
    aaseq[] seqarrayout=new aaseq[individuals];
         
        //System.out.println("seqlength="+seqlength+" indiv="+individuals);
        if((readinfo==0)||(seqlength==0)){
            graph.error("Alignment not in Phylip format");
            return seqarrayout;
        }
       
    int readloop=0;
    int seqoffset=0;
    alignread.mark(2);
    readchar=(char)alignread.read();
    while((Character.isWhitespace(readchar))){//if there was a newline or a space
            alignread.mark(2);//save the current position
            readchar=(char)alignread.read();
        }//if another newline repeat loop
        alignread.reset();//if not, start reading after the last mark;
        
    while(readloop<individuals){//means while i'm doing the first block of the alignment
        aaseq currentread=new aaseq();
        boolean readon=true;
        char[] seqname=new char[10];
        char[] seqseq=new char[seqlength];
        alignread.read(seqname,0,10);//read the first 10 chars and assign them to seqname
        String sequencename=new String(seqname);
        currentread.seqname=String.valueOf(sequencename);
        currentread.seqseq=seqseq;
        seqname=new char[10];
        try{                                    //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
        if(readon){    
        readchar=(char)alignread.read();
        int seqpos=0;
        if(readchar=='?'){
            readchar='-';
        }
        while((readchar!='\n')&&(readchar!='\r')){
            seqseq[seqpos]=readchar;
            seqpos+=1;
            if(Character.isWhitespace(readchar)){//if I have read a space char
                seqpos-=1;                          //don't increment the counter
            }
            readchar=(char)alignread.read();
            if(readchar=='?'){
            readchar='-';
            }
        }//end while
        while((readchar=='\n')||(readchar=='\r')){
            alignread.mark(2);//save the current position
            readchar=(char)alignread.read();
        }//if another newline repeat loop
        alignread.reset();//if not, start reading after the last newline statement;
        for(int i=0;i<seqpos;i++){
            currentread.seqseq[i]=seqseq[i];
        }//end for
        seqoffset=seqpos;
        seqarrayout[readloop]=currentread;
        readloop+=1;
    }//End if readon
    
    }//end while readloop<individuals
    
    boolean readon=true;
    int seqposold=0;
        
    while((readon)&&(readloop>=individuals)){//while it is not end of file and not first pass
        char[] seqseq=new char[seqlength];
        int seqpos=0;
        int seqnumber=0;
        seqnumber=readloop;
        while(seqnumber>=individuals){//in this loop I try to find the sequence the seqseq array belongs to
            seqnumber=seqnumber-individuals;
        }//end while
        if(seqnumber==0){//if the stuff I read belonged to the first sequence
        seqoffset=seqoffset+seqposold;//increase the offset by the length read
        }//end if
        
        try{                                    //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
            
        readchar=(char)alignread.read();
        if(readchar=='?'){
            readchar='-';
        }
        while((readchar!='\n')&&(readchar!='\r')&&(readon==true)){//as long as I'm on the same line of text
            try{                                            //see if there is more to read
                boolean readable=alignread.ready();
                if(readable!=true){
                    readon=false;
                }
            }catch (IOException e){
                readon=false;
                graph.message("EOF reached");
            }
            seqseq[seqpos]=readchar;
            seqpos+=1;
            if(Character.isWhitespace(readchar)){//if I have read a space char don't increment counter
                seqpos-=1;
            }
            readchar=(char)alignread.read();
            if(readchar=='?'){
            readchar='-';
            }
        }//end while
        while((readchar=='\r')||(readchar=='\n')){//if there was a newline
            alignread.mark(2);//save the current position
            readchar=(char)alignread.read();
        }//if another newline repeat loop
        alignread.reset();//if not, start reading after the last newline statement;
        for(int i=0;i<seqpos;i++){//put seqseq in the corresponding aaseq file(between 1 and individuals)
            seqarrayout[seqnumber].seqseq[(seqoffset+i)]=seqseq[i];
        }//end for
        readloop+=1;
        seqposold=seqpos;
    }//end while is is possible to read
    
    alignread.close();
    //testseqvector(seqarrayout);
    //System.out.println("done testseqvector");
    return seqarrayout;
}//end phylipread



//*****************************************************************************
//the treeconread method reads from a file and returns an array of aaseq objects.

public static aaseq[] treeconread(File filenamein)
    throws IOException{
        String aaseqname;
        Vector seqvector = new Vector();
        aaseq seqobj;
        int seqlength, iaa;
        int count=0;
        char seqaa;
        int seqnumber=0;
        BufferedReader alignread = dataf.in(filenamein);
        try{
        seqlength = Integer.parseInt(alignread.readLine());
        }catch (NumberFormatException e){
            graph.message("Invalid Alignment");
            aaseq[] errorseq = new aaseq[0];    
            return (errorseq);
        }//return a dummy aaseq array
        while (((aaseqname=(alignread.readLine()))!=null)){
            if (((aaseqname.length())!=0)){
            count=0;
            seqnumber+=1;
            char [] seqseq = new char[seqlength];
            while (count<(seqlength)){
                iaa=(alignread.read());
                seqaa=(char)iaa;//was :convert.tochar(iaa);
                if (Character.isJavaIdentifierPart(seqaa)||(seqaa=='-')||(seqaa=='?')||(seqaa=='_')){
                    if((Character.isLetter(seqaa))==false){
                        seqaa='-';
                    }
                seqseq [count]= seqaa;
                count += 1;
                }//end of ifcharacterisjavaidentifierpart
                if ((Character.isJavaIdentifierPart(seqaa)||(seqaa=='-')||(seqaa=='?')||(seqaa=='_')||(Character.isWhitespace(seqaa)))==false){
                    graph.error("false sequence length or illegal character:"+seqaa+" at sequence "+aaseqname+" position="+count);
                }
            }//end of while count<seqlenght
        alignread.mark(2);
        seqaa=(char)alignread.read();
        while((Character.isWhitespace(seqaa))){//if there was a newline or a space
            alignread.mark(2);//save the current position
            seqaa=(char)alignread.read();
        }//if another newline repeat loop
        alignread.reset();//if not, start reading after the last mark;
        aaseq currentread = new aaseq();
        currentread.seqname=aaseqname;
        currentread.seqseq=seqseq;
        seqvector.addElement(currentread);
        //System.out.println("name="+aaseqname+" length="+seqlength);
        }//end of if Stringlength!=0
        }//end of while aaseq!=null
    //System.out.println("total sequences: "+ (seqnumber));
    aaseq[] seqarrayout = new aaseq[seqvector.size()];
        seqvector.copyInto(seqarrayout);
        alignread.close();
       // testseqvector(seqarrayout);
    return seqarrayout;
    }//end of treeconread() 
    
   
    //*************************
//is used to test the sequences read in
static void testseqvector(aaseq[] seqarray){
    System.out.println("Starting testseqvector");
    int individuals=java.lang.reflect.Array.getLength(seqarray);
    String seqname;
    for (int i=0;i<individuals;i++){
        System.out.println("name:"+seqarray[i].seqname);
        System.out.println("length:"+java.lang.reflect.Array.getLength(seqarray[i].seqseq));
        for(int j=0;j<java.lang.reflect.Array.getLength(seqarray[i].seqseq);j++){
            System.out.print(seqarray[i].seqseq[j]+",");
        }
        System.out.println("");
    }
}//end testseqvector
    
    
}//end class

