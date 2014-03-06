package al.ali.taxonomy;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class HitDic {
/*
 * 
 * 
 * def __init__(self, path):
    # The creation routine

    self._path = path
    self._queryHit_to_id_dictionary = shelve.open( os.path.join ( self._path, 'queryHit_to_id.data'))
    self._queryHit_to_id_dictionary.close()

  def deleteDic(self):
  #This will delete the old dictionary
    self._queryHit_to_id_dictionary = shelve.open( os.path.join ( self._path, 'queryHit_to_id.data'))
    self._queryHit_to_id_dictionary.clear()
    self._queryHit_to_id_dictionary.close()



  def Pickle(self, list_of_queryHit_objs):
    #  This puts all the query hit ids parsed from the data file into a dictionary

    self._queryHit_to_id_dictionary = shelve.open( os.path.join ( self._path, 'queryHit_to_id.data'))
   
    while len(list_of_queryHit_objs) > 0:
      
      key = str(list_of_queryHit_objs[0]._query_id + "|" + str(list_of_queryHit_objs[0]._hit_no))
      '''if self._queryHit_to_id_dictionary.has_key(key):
        self._queryHit_to_id_dictionary[key] = list_of_queryHit_objs[0]

      else:
        self._queryHit_to_id_dictionary[key] = 'No Hit'
      '''
      self._queryHit_to_id_dictionary[key] = list_of_queryHit_objs[0]
      del list_of_queryHit_objs[0]

    self._queryHit_to_id_dictionary.close()

  def OpenDic(self):
    
    # Open the dictionary for reading
    self._queryHit_to_id_dictionary = shelve.open( os.path.join ( self._path, 'queryHit_to_id.data'),writeback=True)

  def CloseDic(self):
  
    # Close the dictionary
    self._queryHit_to_id_dictionary.close()

  def PrintAllInDic(self):

    # Just for testing
    #  Prints out all the dictionary entries as maps 
    #print "\nquery_def + "" + Hit No. to Tax ID\n"
    self._queryHit_to_id_dictionary = shelve.open( os.path.join ( self._path, 'queryHit_to_id.data'),writeback=True)
    for i in self._queryHit_to_id_dictionary.keys():
      print "Query_def:",self._queryHit_to_id_dictionary[i]._query_id
      print "Hit_No.:",self._queryHit_to_id_dictionary[i]._hit_no
      print "Hit_id.:",self._queryHit_to_id_dictionary[i]._hit_id
      print "Hit_len.:",self._queryHit_to_id_dictionary[i]._hit_len
      print "Species Name:",self._queryHit_to_id_dictionary[i]._name_with_whitespace
      print "Tax_id:",self._queryHit_to_id_dictionary[i]._tax_id
      print "Score:",self._queryHit_to_id_dictionary[i]._score
      print "Evalue:",self._queryHit_to_id_dictionary[i]._evalue
      print "Annotation.:",self._queryHit_to_id_dictionary[i]._annotation
    self._queryHit_to_id_dictionary.close()
######  End of class HitDic
 * 
 */
}
