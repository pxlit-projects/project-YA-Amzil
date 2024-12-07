export class Post {
  id?: number;
  title: string;
  content: string;
  author: string;
  createAt: Date;
  updateAt: Date;
  status: 'DRAFT' | 'PUBLISHED' | 'PENDING';

  constructor(
    // id: number,
    title: string,
    content: string,
    author: string,
    createAt: string,
    updateAt: string,
    status: 'DRAFT' | 'PUBLISHED' | 'PENDING'
  ) {
    // this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.createAt = new Date(createAt);
    this.updateAt = new Date(updateAt);
    this.status = status;
  }
}
