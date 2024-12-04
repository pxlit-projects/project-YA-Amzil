export class Post {
  id?: number;
  title: string;
  content: string;
  category: string;
  author: string;
  createAt: Date;
  updateAt: Date;
  status: string;

  constructor(
    // id: number,
    title: string,
    content: string,
    category: string,
    author: string,
    createAt: Date,
    updateAt: Date,
    status: string
  ) {
    // this.id = id;
    this.title = title;
    this.content = content;
    this.category = category;
    this.author = author;
    this.createAt = createAt;
    this.updateAt = updateAt;
    this.status = status;
  }
}
