package al.ali.taxonomy;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class NameDic {

	/*
	 * 
	 *   def __init__(self, path):
    # The creation routine

    self._path = path

    self._name_to_id_dictionary = shelve.open( os.path.join(self._path, 'name_to_id.data'))
    self._id_to_name_dictionary = shelve.open( os.path.join(self._path, 'id_to_name.data'))

    self._name_to_id_dictionary.close()
    self._id_to_name_dictionary.close()

  def Pickle(self, list_of_name_objs):
    #  This puts all the name ids parsed from the data file into a dictionary

    self._name_to_id_dictionary = shelve.open( os.path.join ( self._path, 'name_to_id.data'))
    self._id_to_name_dictionary = shelve.open( os.path.join ( self._path, 'id_to_name.data'))
   
    while len(list_of_name_objs) > 0:
      # If the current name obj is that of the scientific name for the node, place it in the _id_to_name_dic
      if list_of_name_objs[0]._name_category == 'scientific name':
        self._id_to_name_dictionary[list_of_name_objs[0]._tax_id] = list_of_name_objs[0]._name_with_whitespace

      if self._name_to_id_dictionary.has_key(list_of_name_objs[0]._name):
        self._name_to_id_dictionary[list_of_name_objs[0]._unique] = list_of_name_objs[0]

      else:
        if list_of_name_objs[0]._name:
          self._name_to_id_dictionary[list_of_name_objs[0]._name] = list_of_name_objs[0]
       # Otherwise, if the node has not yet been named, place this name as the _id_to_name_dic
       #  name until the scientific name is encountered
        '''if list_of_name_objs[0]._name_with_whitespace:
          self._id_to_name_dictionary[list_of_name_objs[0]._tax_id] = list_of_name_objs[0]._name_with_whitespace
        else:
          self._id_to_name_dictionary[list_of_name_objs[0]._tax_id] = 'No Name'
        '''#because we just need scientific name for id
      del list_of_name_objs[0]

    self._name_to_id_dictionary.close()
    self._id_to_name_dictionary.close()

  def OpenDic(self):
    
    # Open the dictionary for reading
    #print 'nameDic.opendic=',os.path.join ( self._path, 'name_to_id.data')
    self._name_to_id_dictionary = shelve.open( os.path.join ( self._path, 'name_to_id.data'),writeback=True)
    self._id_to_name_dictionary = shelve.open( os.path.join ( self._path, 'id_to_name.data'),writeback=True)

  def CloseDic(self):
  
    # Close the dictionary
    self._name_to_id_dictionary.close()
    self._id_to_name_dictionary.close()

  def PrintAllInDic(self):

    # Just for testing
    #  Prints out all the dictionary entries as maps 
    print "\nName to Tax ID\n"
    for i in self._name_to_id_dictionary.keys():
      print self._name_to_id_dictionary[i]

######  End of class NameDic
	 * 
	 */
}
