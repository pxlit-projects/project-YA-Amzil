export class Review {
  id?: number;
  postId?: number;
  reviewer: string;
  comment: string;
  reviewedAt: Date;

  constructor(
    reviewer: string,
    comment: string,
    reviewedAt: string,
  ) {
    this.reviewer = reviewer;
    this.comment = comment;
    this.reviewedAt = new Date(reviewedAt);
  }
}
