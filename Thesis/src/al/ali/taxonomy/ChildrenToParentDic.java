package al.ali.taxonomy;


/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class ChildrenToParentDic {

	/*
	 * 
	 *  def __init__(self, path):
    self._path = path

  def Pickle(self, child_to_parent_dic):

    child_list = child_to_parent_dic.keys()

    self._child_to_parent_dictionary = shelve.open( os.path.join ( self._path, 'child_to_parent.data'))

    while len(child_list) > 0:
      self._child_to_parent_dictionary[child_list[0]] = child_to_parent_dic[child_list[0]]
      del child_list[0]
    
    self._child_to_parent_dictionary.close()

  def OpenDic(self):
    
    # Open the dictionary for reading
    self._child_to_parent_dictionary = shelve.open( os.path.join ( self._path, 'child_to_parent.data'))

  def CloseDic(self):
  
    # Close the dictionary
    self._child_to_parent_dictionary.close()

  def PrintAllInDic(self):

    # Just for testing
    #  Prints out all the dictionary entries as maps

    self._child_to_parent_dictionary = shelve.open( os.path.join ( self._path, 'child_to_parent.data'))
    child_list = self._child_to_parent_dictionary.keys()
    #print "list:",child_list
    print "Mapping tax_id to lineage"
    for i in self._child_to_parent_dictionary.keys():
      print i, self._child_to_parent_dictionary[i]
    self._child_to_parent_dictionary.close()

	 * 
	 */
}
