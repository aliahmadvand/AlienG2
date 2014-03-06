package al.ali.taxonomy;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class ParentToChildrenDic {
/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 *   def __init__(self, path):
    self._path = path

  def Pickle(self, parent_to_child_dic):

    parent_list = parent_to_child_dic.keys()

    self._parent_to_child_dictionary = shelve.open( os.path.join ( self._path, 'parent_to_child.data'))

    while len(parent_list) > 0:
      self._parent_to_child_dictionary[parent_list[0]] = parent_to_child_dic[parent_list[0]]
      del parent_list[0]
    
    self._parent_to_child_dictionary.close()

  def OpenDic(self):
    
    # Open the dictionary for reading
    self._parent_to_child_dictionary = shelve.open( os.path.join ( self._path, 'parent_to_child.data'))

  def CloseDic(self):
  
    # Close the dictionary
    self._parent_to_child_dictionary.close()

  def PrintAllInDic(self):

    # Just for testing
    #  Prints out all the dictionary entries as maps

    self._parent_to_child_dictionary = shelve.open( os.path.join ( self._path, 'parent_to_child.data'))
    for i in self._parent_to_child_dictionary.keys():
      print i, self._parent_to_child_dictionary[i]
    self._id_to_node_dictionary.close()

###### End of class ParentToChildrenDic
 */
}
