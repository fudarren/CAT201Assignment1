function ShopByCategory() {
  return (
    <>
      <div className="shop-by-category">
        <h2>Shop by Category</h2>
        <div className="categories">
          <a href="#" className="category">
            <img src="src/assets/guitar.svg" alt="Guitars" />
            <span>Guitars</span>
          </a>
          <a href="#" className="category">
            <img src="src/assets/bass.svg" alt="Basses" />
            <span>Basses</span>
          </a>
          <a href="#" className="category">
            <img src="src/assets/drum.svg" alt="Drums" />
            <span>Drums</span>
          </a>

          <a href="#" className="category">
            <img src="src/assets/keyboard.svg" alt="Keyboards" />
            <span>Keyboards</span>
          </a>
        </div>
      </div>
    </>
  );
}

export default ShopByCategory;
