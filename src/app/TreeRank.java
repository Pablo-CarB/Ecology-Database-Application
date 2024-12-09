package app;

/**
 * Represents the rank that a taxa can occupy on the tree of life,
 * with the most general (ie the root node) being designated as root
 */
public enum TreeRank {
  Root("Root"),
  Domain("Domain"),
  Kingdom("Kingdom"),
  Phylum("Phylum"),
  Class("Class"),
  Order("Order"),
  Family("Family"),
  Genus("Genus"),
  Species("Species");

  private String title;
  private TreeRank(String title){
    this.title = title;
  }

  public String getTitle(){return this.title;}

  public TreeRank descendHeirarchy(){
    if(this.ordinal() == TreeRank.Species.ordinal()){
      return null;
    }
    else{
      return TreeRank.values()[this.ordinal() + 1];
    }
  }

  public TreeRank ascendHeirarchy(){
    if(this.ordinal() == TreeRank.Root.ordinal()){
      return null;
    }
    else{
      return TreeRank.values()[this.ordinal() - 1];
    }
  }

}
