package al.ali.taxonomy;

/**
 * @author AHMADVANDA11
 * @date Feb 2, 2013
 */
public class NodeDic {

	
	/*
	 * 
	 * 
# Global variables
# I will try to make these private, and have routines to pass them to a
# calling function
  

  def __init__(self, path):

    # The creation routine
    #  This puts all the tax ids parsed from the data file into a dictionay
    #  They will be linked both forwards and reversely until I know a better way to do it

    self._path = path
    #print 'path----------',self._path
    self._id_to_node_dictionary = shelve.open( os.path.join ( self._path, 'id_to_node.data'))
    #self._id_to_node_dictionary = shelve.open(r'F:\Bio-project\2009summer\Jing\tax1\id_to_node.data')
    self._id_to_node_dictionary.close()

  def Pickle(self,list_of_node_objs):

    #  This puts all the tax ids parsed from the data file into a dictionay
    #  They will be linked both forwards and reversely until I know a better way to do it

    self._id_to_node_dictionary = shelve.open( os.path.join ( self._path, 'id_to_node.data'))

    while len(list_of_node_objs) > 0:
      self._id_to_node_dictionary[list_of_node_objs[0]._tax_id] = list_of_node_objs[0]
      del list_of_node_objs[0]
    self._id_to_node_dictionary.close()
    
  def OpenDic(self):
    
    # Open the dictionary for reading
    self._id_to_node_dictionary = shelve.open( os.path.join ( self._path, 'id_to_node.data'))

  def CloseDic(self):
  
    # Close the dictionary
    self._id_to_node_dictionary.close()

  def PrintAllInDic(self):

    # Just for testing
    #  Prints out all the dictionary entries as maps
    
    print "\nNode ID to Object dictionary\n", 
    for i in self._id_to_node_dictionary.keys():
      print self._id_to_node_dictionary[i]

	 * 
	 * 
	 */
}
